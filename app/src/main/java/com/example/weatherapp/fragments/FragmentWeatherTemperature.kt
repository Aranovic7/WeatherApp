package com.example.weatherapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adaptors.HourlyAdaptor
import com.example.weatherapp.adaptors.WeeklyAdaptor
import com.example.weatherapp.databinding.FragmentWeatherTemperatureBinding
import com.example.weatherapp.dataclasses.WeatherDataClass
import com.example.weatherapp.sealedclasses.DataStates
import com.example.weatherapp.utils.getCelsiusTemperature
import com.example.weatherapp.utils.getCityName
import com.example.weatherapp.utils.getTime
import com.example.weatherapp.utils.setImage
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

class FragmentWeatherTemperature : BaseFragment() {
    private lateinit var binding: FragmentWeatherTemperatureBinding
    private var hourlyAdaptor: HourlyAdaptor? = null
    private var weeklyAdaptor: WeeklyAdaptor? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherTemperatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setOnRefreshListener {
            loadWeatherInfo()
        }
        initRecylerViews()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mViewModel.weatherData.collectLatest {
                when (it) {
                    is DataStates.Loading -> {
                        binding.root.isRefreshing = true
                    }
                    is DataStates.Initial -> {}
                    is DataStates.Success<*> -> {
                        binding.root.isRefreshing = false
                        bindData(it.value as WeatherDataClass?)
                    }
                    is DataStates.Error -> {
                        binding.root.isRefreshing = false
                    }
                }
            }
        }
    }

    private fun initRecylerViews() {
        hourlyAdaptor = HourlyAdaptor(requireContext())
        weeklyAdaptor = WeeklyAdaptor(requireContext())
        with(binding) {
            hourlyRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = hourlyAdaptor
            }
            weeklyRecycler.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter = weeklyAdaptor
            }
        }
    }


    private fun bindData(value: WeatherDataClass?) {
        with(binding) {
            tvWeatherTem.text = value?.current?.temp?.toFloat()?.getCelsiusTemperature()
            val roundedPressure =
                (value?.current?.pressure?.toFloat() ?: 0f).roundToInt()
            tvPressure.text = "$roundedPressure hPa"
            tvHumadity.text = Math.round(
                value?.current?.humidity?.toFloat() ?: 0f
            ).toString() + " %"
            val windSpeed = value?.current?.windSpeed?.toFloat() ?: 0f * 18 / 5
            tvWind.text = Math.round(windSpeed).toString() + " km/h"
            tvVisibility.text = value?.current?.visibility?.toString()
            weatherIcon.setImage(R.drawable.light_cloud)
            val address = context?.getCityName(Pair(value?.lat!!, value.lon))
            tvCityName.text = address?.get(0)?.subAdminArea + ", " + address?.get(0)?.countryName
            tvCloudy.text = "Cloudy ${value?.current?.clouds}%"
            tvFeels.text = value?.current?.feelsLike?.toFloat()?.getCelsiusTemperature()
            tvTime.text = System.currentTimeMillis().getTime()

            hourlyAdaptor?.submitData(value?.hourly ?: listOf())
            weeklyAdaptor?.submitData(value?.daily ?: listOf())
        }
    }


}