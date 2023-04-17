package com.example.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.dataclasses.HistoryDataClass

@Database(entities = [HistoryDataClass::class], version = 2)
abstract class DataBaseUtil : RoomDatabase() {
    abstract fun dao(): AppDao
}