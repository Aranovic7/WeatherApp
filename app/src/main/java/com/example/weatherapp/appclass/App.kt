package com.example.weatherapp.appclass;

import android.app.Application
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.room.Room
import com.example.weatherapp.api.WebClient
import com.example.weatherapp.datarepositories.WeatherApiRepo
import com.example.weatherapp.db.DataBaseUtil
import com.example.weatherapp.viewmodels.TemperatureSharedViewModel
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@App)
            modules(
                listOf(
                    appModule
                )
            )
        }
    }

    private val appModule = module {
        single { WebClient(androidContext()) }
        viewModel {
            TemperatureSharedViewModel(
                WeatherApiRepo(
                    androidContext(), get(), databaseUtils
                )
            )
        }
        single { getSystemService(Context.LOCATION_SERVICE) as LocationManager }
        single { getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }
        factory { getGoogleApiClient(androidContext()) }
        factory { getLocationRequest() }
    }
    private val databaseUtils: DataBaseUtil
        get() = Room.databaseBuilder(this, DataBaseUtil::class.java, "WeatherAppDataBase")
            .fallbackToDestructiveMigration()
            .build()

    private fun getGoogleApiClient(context: Context): GoogleApiClient {
        val googleApiClient = GoogleApiClient.Builder(context).addApi(LocationServices.API)
            .addConnectionCallbacks(object : GoogleApiClient.ConnectionCallbacks {
                override fun onConnected(bundle: Bundle?) {}
                override fun onConnectionSuspended(i: Int) {
                }
            })
            .addOnConnectionFailedListener { connectionResult ->

            }

        return googleApiClient.build()
    }

    private fun getLocationRequest(): LocationSettingsRequest.Builder {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (30 * 1000).toLong()
        locationRequest.fastestInterval = (5 * 1000).toLong()
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        return builder
    }


}
