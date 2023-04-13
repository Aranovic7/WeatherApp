package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adaptors.DateAdaptor
import com.example.weatherapp.databinding.FragmentWindBinding
import com.example.weatherapp.dataclasses.Daily
import com.example.weatherapp.dataclasses.DateDataClass
import com.example.weatherapp.dataclasses.WeatherDataClass
import com.example.weatherapp.sealedclasses.DataStates
import com.example.weatherapp.utils.getCityName
import com.example.weatherapp.utils.getDate
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

class WindFragment : BaseFragment() {
    private lateinit var binding: FragmentWindBinding
    private var mAdaptor: DateAdaptor? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWindBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
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

    private fun bindData(weatherDataClass: WeatherDataClass?) {
        val dd = ArrayList<DateDataClass>()
        weatherDataClass?.daily?.forEach {
            dd.add(DateDataClass(it, false))
        }
        mAdaptor?.submitData(dd)
        mAdaptor?.selectDate(0)
        with(binding) {
            val address = context?.getCityName(Pair(weatherDataClass?.lat!!, weatherDataClass.lon))
            tvCityName.text = address?.get(0)?.subAdminArea + ", " + address?.get(0)?.countryName
            tvTime.text = weatherDataClass?.current?.dt?.toLong()?.getDate()
        }

        showWindData(weatherDataClass?.daily?.get(0)!!)
    }

    private fun showWindData(daily: Daily) {
        with(binding) {
            val windSpeed = daily.windSpeed.toFloat()
            tvWind.text = windSpeed.roundToInt().toString() + " km/h"
            tvWindDirection.text = getDirectionFromDegrees(daily.windDeg.toFloat())
        }

    }

    private fun getConvertedSpeed(speed: Float): String {
        val mph = (speed / 2.24).toInt()
        return mph.toString()
    }

    private fun getDirectionFromDegrees(degree: Float): String {
        val directionsArrays = arrayOf(
            "N",
            "NNE",
            "NE",
            "ENE",
            "E",
            "ESE",
            "SE",
            "SSE",
            "S",
            "SSW",
            "SW",
            "WSW",
            "W",
            "WNW",
            "NW",
            "NNW"
        )
        val moduleInd = (degree + 11.25) / 22.5
        val index = moduleInd % 16
        return directionsArrays[index.toInt()]
    }


    private fun initRecycler() {
        mAdaptor = DateAdaptor(requireContext())
        binding.dateRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdaptor
        }
        mAdaptor?.onItemClick = { value, position ->
            showWindData(value)
        }
    }

}