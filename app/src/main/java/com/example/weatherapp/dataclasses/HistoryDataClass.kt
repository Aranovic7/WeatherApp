package com.example.weatherapp.dataclasses

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historyTb")
data class HistoryDataClass(
    val city: String,
    val lat: Double,
    val lon: Double,
    val clouds: Int,
    val dewPoint: Double,
    val dt: Int,
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val rain: Rain,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val uvi: Double,
    val visibility: Int,
    val windDeg: Double,
    val windSpeed: Double,
    var time: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}