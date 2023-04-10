package com.example.weatherapp.sealedclasses

sealed class DataStates {
    object Loading : DataStates()
    object Initial : DataStates()
    class Success<T>(val value: T) : DataStates()
    class Error(val message: String) : DataStates()
}
