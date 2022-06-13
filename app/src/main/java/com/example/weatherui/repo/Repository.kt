package com.example.weatherui.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherui.api.ApiClient
import com.example.weatherui.model.ResponseFail
import com.example.weatherui.model.WeatherAPI
import com.example.weatherui.util.AuthState
import retrofit2.Response

class Repository( private val detailApi: ApiClient,
                  private val applicationContext: Context) {

    private val weatherResult = MutableLiveData<AuthState<WeatherAPI>>()
    val weatherLiveData: LiveData<AuthState<WeatherAPI>>
        get() = weatherResult

    private val weatherResponse = MutableLiveData<ResponseFail>()
    val weatherLiveData_: LiveData<ResponseFail>
        get() = weatherResponse

    suspend fun getWeather(q: String) {
       try{
           val result_ = detailApi.getWeather(q)
          if (result_.body() != null) {
               weatherResult.postValue(AuthState.Success(result_.body()!!))
           }
           else{
               weatherResult.postValue(AuthState.AuthError("e.message.toString()"))
           }
       }catch (e: Exception){
           weatherResult.postValue(AuthState.AuthError(e.message.toString()))
       }
    }
}