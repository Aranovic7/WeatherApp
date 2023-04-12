package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.adaptors.SunAdaptor

import com.example.weatherapp.databinding.FragmentSunBinding
import com.example.weatherapp.dataclasses.WeatherDataClass
import com.example.weatherapp.sealedclasses.DataStates
import com.example.weatherapp.utils.getCityName
import com.example.weatherapp.utils.getDate
import com.example.weatherapp.utils.getTime
import kotlinx.coroutines.flow.collectLatest

class SunFragment : BaseFragment() {
    private lateinit var binding: FragmentSunBinding
    private var sunAdaptor: SunAdaptor? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSunBinding.inflate(inflater, container, false)
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
        sunAdaptor = SunAdaptor(requireContext())
        binding.sunRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = sunAdaptor
        }
    }

    private fun bindData(weatherDataClass: WeatherDataClass?) {
        with(binding) {
            val address = context?.getCityName(Pair(weatherDataClass?.lat!!, weatherDataClass.lon))
            tvCityName.text = address?.get(0)?.subAdminArea + ", " + address?.get(0)?.countryName
            tvTime.text = weatherDataClass?.current?.dt?.toLong()?.getDate()
            tvSunRise.text = weatherDataClass?.current?.sunrise?.toLong()?.getTime()
            tvSunset.text = weatherDataClass?.current?.sunset?.toLong()?.getTime()

            sunAdaptor?.submitData(weatherDataClass?.daily ?: listOf())
        }
    }

}