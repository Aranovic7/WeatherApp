package com.example.weatherapp.fragments

import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.adaptors.ButtonsAdaptor
import com.example.weatherapp.databinding.FragmentMainBinding
import com.example.weatherapp.dataclasses.ButtonDataClass
import com.example.weatherapp.dataclasses.WeatherDataClass
import com.example.weatherapp.sealedclasses.DataStates
import com.example.weatherapp.utils.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest


class MainFragment : BaseFragment() {

    private lateinit var binding: FragmentMainBinding
    private var mButtonAdaptor: ButtonsAdaptor? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtonRecycler()
        if (haveNetworkConnection()) {
            if (hasLocationPermission()) {
                if (haveDeviceGPS()) {
                    if (hasGpsEnable()) {
                        if (savedInstanceState == null && mViewModel.weatherData.value !is DataStates.Success<*>) {
                            loadWeatherInfo()
                        }
                    } else {
                        enableLoc()
                    }
                }
            } else {
                openPermissionDialog("App need location permission to get weather temperature \nAllow for better use.")
            }
        }
        with(binding) {
            root.setOnRefreshListener {
                loadData()
            }

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            mViewModel.weatherData.collectLatest {
                when (it) {
                    is DataStates.Loading -> {
                        binding.root.isRefreshing = true
                    }
                    is DataStates.Initial -> {}
                    is DataStates.Success<*> -> {
                        if (binding.root.isRefreshing) {
                            binding.root.isRefreshing = false
                        }
                        bindData(it.value as WeatherDataClass?)
                    }
                    is DataStates.Error -> {
                        if (binding.root.isRefreshing) {
                            binding.root.isRefreshing = false
                        }
                    }
                }
            }
        }

    }

    private fun initButtonRecycler() {
        mButtonAdaptor = ButtonsAdaptor(requireContext())
        binding.moreButtons.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mButtonAdaptor
        }
        mButtonAdaptor?.onButtonClick = { value, position ->
            val id = value.action as Int
            if (id != R.id.mainFragment) {
                findNavController().navigate(id)
            }

            Unit
        }
        loadButtons()
    }

    private fun loadButtons() {
        val list = listOf(
            ButtonDataClass(
                getString(R.string.weekly_forecast),
                R.drawable.ic_tem_weather,
                R.id.action_mainFragment_to_fragmentWeatherTemperature
            ),
            ButtonDataClass(
                getString(R.string.sunr),
                R.drawable.sunny,
                R.id.action_mainFragment_to_sunFragment
            ),
            ButtonDataClass(
                getString(R.string.uv),
                R.drawable.uv_index,
                R.id.action_mainFragment_to_UVFragment
            ),
            ButtonDataClass(
                getString(R.string.win),
                R.drawable.air_fan,
                R.id.action_mainFragment_to_windFragment
            ),
            ButtonDataClass(
                getString(R.string.hu),
                R.drawable.humidity,
                R.id.action_mainFragment_to_humidityFragment
            ),
            ButtonDataClass(getString(R.string.ra), R.drawable.rain, R.id.action_mainFragment_to_rainFragment),
            ButtonDataClass(getString(R.string.pr), R.drawable.pressure, R.id.mainFragment)
        )
        mButtonAdaptor?.submitButtons(list)
    }

    private fun loadData() {
        if (haveNetworkConnection()) {
            if (hasLocationPermission()) {
                if (haveDeviceGPS()) {
                    if (hasGpsEnable()) {
                        loadWeatherInfo()
                    } else {
                        enableLoc()
                    }
                }
            } else {
                openPermissionDialog("App need location permission to get weather temperature \nAllow for better use.")
            }
        }
    }

    private fun bindData(value: WeatherDataClass?) {
        with(binding) {
            tvWeatherTem.text = (value?.current?.temp?.toFloat() ?: 0f).getCelsiusTemperature()
            weatherIcon.setImage(R.drawable.light_cloud)
            val address = context?.getCityName(Pair(value?.lat!!, value.lon))
            tvCityName.text = address?.get(0)?.subAdminArea + ", " + address?.get(0)?.countryName
            tvCloudy.text = getString(R.string.cloudy) + " ${value?.current?.clouds}%"
            tvFeels.text = getString(R.string.feellike) + (value?.current?.feelsLike?.toFloat()
                ?: 0f).getCelsiusTemperature()
        }
    }


    private fun openPermissionDialog(message: String?) {
        AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            .setTitle(getString(R.string.permissionalert))
            .setMessage(message)
            .setPositiveButton(getText(R.string.allow)) { dialog, which -> checkLocationPermission() }
            .setNegativeButton("No Thanks", null)
            .show()
    }

    private fun enableLoc() {
        googleApiClient.connect()
        locationRequestBuilder.setAlwaysShow(true)
        val result = LocationServices.SettingsApi.checkLocationSettings(
            googleApiClient, locationRequestBuilder.build()
        )
        result.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                    status.startResolutionForResult(
                        requireActivity(),
                        REQUEST_LOCATION
                    )

                } catch (e: IntentSender.SendIntentException) {
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constant.REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (haveDeviceGPS()) {
                        if (hasGpsEnable()) {
                            loadWeatherInfo()
                        }
                    }
                } else {
                    binding.root.showSnackBar(
                        getString(R.string.qq),
                        Snackbar.LENGTH_LONG,
                        getString(R.string.allow)
                    ) {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", requireContext().packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }

                }
            }
        }
    }

    val REQUEST_LOCATION = 199
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_LOCATION -> {
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    loadWeatherInfo()
                }
            }

        }
    }

}