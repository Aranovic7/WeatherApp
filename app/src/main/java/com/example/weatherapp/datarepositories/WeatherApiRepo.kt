package com.example.weatherapp.datarepositories

import android.content.Context
import com.example.weatherapp.api.WebClient
import com.example.weatherapp.dataclasses.WeatherDataClass
import com.example.weatherapp.interfaces.ResultCall
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherApiRepo(
    val context: Context,
    val webClient: WebClient
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
                            val current = it.current

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