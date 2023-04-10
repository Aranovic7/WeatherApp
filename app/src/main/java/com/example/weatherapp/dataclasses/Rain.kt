package com.example.weatherapp.dataclasses



import com.google.gson.annotations.SerializedName


data class Rain(
    @SerializedName("1h")
    val h: Double
)