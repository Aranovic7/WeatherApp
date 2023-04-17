package com.example.weatherapp.datarepositories

import android.content.Context
import com.example.weatherapp.api.WebClient
import com.example.weatherapp.dataclasses.HistoryDataClass
import com.example.weatherapp.dataclasses.WeatherDataClass
import com.example.weatherapp.db.DataBaseUtil
import com.example.weatherapp.interfaces.ResultCall
import com.example.weatherapp.utils.getCityName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherApiRepo(
    val context: Context,
    val webClient: WebClient, val databaseUtils: DataBaseUtil
) {
    fun loadWeatherInfo(location: Pair<Double, Double>, resultCall: ResultCall<WeatherDataClass?>) {
        webClient.api()
            ?.getWeatherInfo(
                location.first,
                location.second,
                key = "51192fe1bed247a9070ddf9dd0c64e7c"
            )
            ?.enqueue(object : Callback<WeatherDataClass> {
                override fun onResponse(
                    call: Call<WeatherDataClass>,
                    response: Response<WeatherDataClass>
                ) {
                    if (response.isSuccessful) {
                        val dd = response.body()
                        dd?.let {
                            val address = context.getCityName(Pair(it.lat, it.lon))
                            val current = it.current
                            val city =
                                address?.get(0)?.subAdminArea + ", " + address?.get(0)?.countryName
                            val history = HistoryDataClass(
                                city,
                                it.lat,
                                it.lon,
                                current.clouds,
                                current.dewPoint,
                                current.dt,
                                current.feelsLike,
                                current.humidity,
                                current.pressure,
                                current.rain,
                                current.sunrise,
                                current.sunset,
                                current.temp,
                                current.uvi,
                                current.visibility,
                                current.windDeg,
                                current.windSpeed,
                                System.currentTimeMillis()
                            )
                            databaseUtils.dao().insertRecord(history)
                            resultCall.resultSuccess(dd)
                        }
                    } else {
                        resultCall.onFail("Data not found")
                    }
                }

                override fun onFailure(call: Call<WeatherDataClass>, t: Throwable) {

                    resultCall.onFail(t.message ?: "Fail to get")
                }

            })
    }


}