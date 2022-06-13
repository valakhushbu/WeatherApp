package com.example.weatherui.api

import com.example.weatherui.model.ResponseFail
import com.example.weatherui.model.WeatherAPI
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiClient {

    @GET("data/2.5/weather?&units=metric&appid=a5ef4993fd3a4f91de3fc4615ee5ba08")
    suspend fun getWeather(@Query("q") search:String) : Response<WeatherAPI>

}