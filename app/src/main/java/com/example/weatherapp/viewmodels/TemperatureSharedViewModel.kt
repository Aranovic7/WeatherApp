package com.example.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.dataclasses.HistoryDataClass
import com.example.weatherapp.dataclasses.WeatherDataClass
import com.example.weatherapp.datarepositories.WeatherApiRepo
import com.example.weatherapp.interfaces.ResultCall
import com.example.weatherapp.sealedclasses.DataStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TemperatureSharedViewModel(private val weatherApiRepo: WeatherApiRepo) : ViewModel() {
    var isRun = false
    private val _weatherData: MutableStateFlow<DataStates> = MutableStateFlow(DataStates.Initial)
    val weatherData = _weatherData.asStateFlow()
    private val _history: MutableLiveData<List<HistoryDataClass>> = MutableLiveData()
    val history: LiveData<List<HistoryDataClass>> = _history


    fun setWeatherState(state: DataStates) {
        _weatherData.value = state
    }


    fun loadWeatherInfo(location: Pair<Double, Double>) = viewModelScope.launch(Dispatchers.IO) {
        _weatherData.value = DataStates.Loading
        weatherApiRepo.loadWeatherInfo(location, object : ResultCall<WeatherDataClass?>() {
            override fun onFail(message: String) {
                super.onFail(message)
                _weatherData.value = DataStates.Error(message)
            }

            override fun resultSuccess(result: WeatherDataClass?) {
                super.resultSuccess(result)
                _weatherData.value = DataStates.Success(result)
            }
        })
    }

    fun loadHistory() = viewModelScope.launch(Dispatchers.IO) {
        _history.postValue(weatherApiRepo.databaseUtils.dao().getRecords())
    }

    fun deleteHistory(item: HistoryDataClass) = viewModelScope.launch(Dispatchers.IO) {
        weatherApiRepo.databaseUtils.dao().deleteRecord(item)
        loadHistory()
    }


}