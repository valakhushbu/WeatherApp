package com.example.weatherui.util

import androidx.room.TypeConverter
import com.example.weatherui.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DataConverter {

    //Weather list DataClass
    @TypeConverter
    fun fromWeather(value: List<Weather>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toWeather(value: String): List<Weather> {
        val gson = Gson()
        val type = object : TypeToken<List<Weather>>() {}.type
        return gson.fromJson(value, type)
    }

  /*  //Wind DataClass
    @TypeConverter
    fun fromWind(wind: Wind): String {
        val gson = Gson()
        val type = object : TypeToken<Wind>() {}.type
        return gson.toJson(wind, type)
    }

    @TypeConverter
    fun toWind(wind: String): Wind {
        val gson = Gson()
        val type = object : TypeToken<Wind>() {}.type
        return gson.fromJson(wind, type)
    }*/
}