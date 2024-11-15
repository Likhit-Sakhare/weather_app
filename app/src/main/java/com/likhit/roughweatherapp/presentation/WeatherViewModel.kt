package com.likhit.roughweatherapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.likhit.roughweatherapp.data.api.Constant
import com.likhit.roughweatherapp.data.api.NetworkResponse
import com.likhit.roughweatherapp.data.api.RetrofitInstance
import com.likhit.roughweatherapp.data.model.WeatherModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

class WeatherViewModel():ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult =
        MutableStateFlow<NetworkResponse<WeatherModel>?>(null)
    val weatherResult = _weatherResult.asStateFlow()

    //Whenever we click on the Search Icon we will get data from the Retrofit
    fun getData(city: String){
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if(response.isSuccessful){
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                }else{
                    _weatherResult.value = NetworkResponse.Error("Failed to load the weather")
                }
            }catch (e: Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to load the weather")
            }
        }
    }
}