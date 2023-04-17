package com.example.weatherapp.db

import androidx.room.*
import com.example.weatherapp.dataclasses.HistoryDataClass

@Dao
interface AppDao {
    @Insert
    fun insertRecord(value: HistoryDataClass): Long

    @Delete
    fun deleteRecord(value: HistoryDataClass)

    @Query("Select * from historyTb")
    fun getRecords(): List<HistoryDataClass>
}