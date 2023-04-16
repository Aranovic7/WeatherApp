package com.example.weatherapp.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationSettingsRequest

import com.example.weatherapp.sealedclasses.DataStates
import com.example.weatherapp.utils.Constant
import com.example.weatherapp.viewmodels.TemperatureSharedViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

open class BaseFragment : Fragment() {
    protected val mViewModel: TemperatureSharedViewModel by sharedViewModel()

    protected val connectivityManager: ConnectivityManager by inject()
    protected val locationManager: LocationManager by inject()
    protected val googleApiClient: GoogleApiClient by inject()
    protected val locationRequestBuilder: LocationSettingsRequest.Builder by inject()

    fun checkLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
                    != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constant.REQUEST_LOCATION_PERMISSION
            )
            return
        }
    }

    fun hasLocationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED)
        } else true
    }

    fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val netInfo = connectivityManager.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals(
                    "WIFI",
                    ignoreCase = true
                )
            ) if (ni.isConnectedOrConnecting) haveConnectedWifi = true
            if (ni.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (ni.isConnectedOrConnecting) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

    fun haveDeviceGPS(): Boolean {

        val providers = locationManager.allProviders
        return providers.contains(LocationManager.GPS_PROVIDER)
    }

    fun hasGpsEnable(): Boolean {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }



    @SuppressLint("MissingPermission")
    protected fun loadWeatherInfo() {
        mViewModel.isRun = false
        mViewModel.setWeatherState(DataStates.Loading)
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 0L, 0F
        ) {
            if (!mViewModel.isRun) {
                mViewModel.isRun = true
                mViewModel.loadWeatherInfo(Pair(it.latitude, it.longitude))
            }

        }
    }
    protected fun loadWeatherInfo(pair: Pair<Double,Double>) {
        mViewModel.isRun = false
        mViewModel.setWeatherState(DataStates.Loading)
        if (!mViewModel.isRun) {
            mViewModel.isRun = true
            mViewModel.loadWeatherInfo(pair)
        }
    }
}