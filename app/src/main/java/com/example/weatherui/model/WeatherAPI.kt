package com.example.weatherui.model

import androidx.room.*
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather",indices=[Index(value = ["city"],unique = true)])
data class WeatherAPI(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val city: String,
    @Embedded
    val clouds: Clouds,
    @Embedded
    val coord: Coord,
    @Embedded
    val main: Main,
    val name: String,
    @Embedded
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    @Embedded
    val wind: Wind
)