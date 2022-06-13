package com.example.weatherui.db

import androidx.room.*
import androidx.room.Dao
import com.example.weatherui.model.WeatherAPI

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherAPI: WeatherAPI)

    @Update
    fun update(weatherAPI: WeatherAPI)

}