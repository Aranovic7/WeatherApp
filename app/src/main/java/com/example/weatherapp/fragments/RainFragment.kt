package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adaptors.DateAdaptor
import com.example.weatherapp.adaptors.HourlyAdaptor
import com.example.weatherapp.databinding.FragmentRainBinding
import com.example.weatherapp.dataclasses.Daily
import com.example.weatherapp.dataclasses.DateDataClass
import com.example.weatherapp.dataclasses.WeatherDataClass
import com.example.weatherapp.sealedclasses.DataStates
import com.example.weatherapp.utils.getCityName
import com.example.weatherapp.utils.getDate
import kotlinx.coroutines.flow.collectLatest

class RainFragment : BaseFragment() {
    private lateinit var binding: FragmentRainBinding
    private var mAdaptor: DateAdaptor? = null
    private var hourlyAdaptor: HourlyAdaptor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRainBinding.inflate(inflater, container, false)
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

    private fun initRecycler() {
        hourlyAdaptor = HourlyAdaptor(requireContext())
        mAdaptor = DateAdaptor(requireContext())
        binding.dateRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdaptor
        }
        binding.hourlyRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdaptor
        }
        mAdaptor?.onItemClick = { value, position ->
            showHumidityData(value)
        }
    }

    private fun showHumidityData(daily: Daily) {
        with(binding) {
            tvRain.text = "${daily.rain}mm"
        }
        val dd =
            ((mViewModel.weatherData.value as DataStates.Success<*>).value as WeatherDataClass?)?.hourly
        val hh = dd?.filter { it.dt.toLong().getDate() == daily.dt.toLong().getDate() }
        hourlyAdaptor?.submitData(hh ?: listOf())

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

        showHumidityData(weatherDataClass?.daily?.get(0)!!)
    }
}