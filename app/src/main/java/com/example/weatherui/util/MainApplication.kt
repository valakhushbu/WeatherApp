package com.example.weatherui.util

import android.app.Application
import com.example.weatherui.api.ApiClient
import com.example.weatherui.api.ApiService
import com.example.weatherui.api.ApiService.getInstance
import com.example.weatherui.repo.Repository

class MainApplication : Application(){

    lateinit var repo : Repository
    override fun onCreate() {
        super.onCreate()

        initialize_()
    }

    private fun initialize_(){
        val api = ApiService.getInstance().create(ApiClient::class.java)
        repo = Repository(api,applicationContext)
    }
}