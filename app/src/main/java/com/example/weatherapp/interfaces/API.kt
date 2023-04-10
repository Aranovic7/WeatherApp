package com.example.weatherapp.interfaces

import com.example.weatherapp.dataclasses.WeatherDataClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface API {
    @GET("onecall")
    fun getWeatherInfo(
        @Query("lat") latitude: Double,
        @Query("lon") lognitude: Double,
        @Query("appid") key: String
    ): Call<WeatherDataClass>
}