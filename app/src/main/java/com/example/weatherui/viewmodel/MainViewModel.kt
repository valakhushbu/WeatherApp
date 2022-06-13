package com.example.weatherui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherui.model.Main
import com.example.weatherui.model.WeatherAPI
import com.example.weatherui.repo.Repository
import com.example.weatherui.util.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repoHelper: Repository): ViewModel() {

       fun getWeather(q:String) {
        viewModelScope.launch { Dispatchers.IO
            repoHelper.getWeather(q)
        }
   }
    val weatherResult : LiveData<AuthState<WeatherAPI>>
        get() = repoHelper.weatherLiveData
}