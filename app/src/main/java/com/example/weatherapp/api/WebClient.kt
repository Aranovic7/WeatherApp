package com.example.weatherapp.api

import android.content.Context
import com.example.weatherapp.interfaces.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WebClient(val context: Context) {
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private var myApi: API? = null

    init {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        myApi = retrofit.create(API::class.java)
    }

    fun api(): API? {
        return myApi
    }

}