package com.example.weatherui.db

import android.content.Context
import androidx.room.*
import com.example.weatherui.model.WeatherAPI
import com.example.weatherui.util.DataConverter

@Database(entities = [WeatherAPI::class],version = 7)
@TypeConverters(DataConverter::class)
abstract class DBHelper: RoomDatabase() {

    abstract fun dao() : Dao

    companion object{
        @Volatile
        private var INSATNCE: DBHelper? = null
        fun getDatabase(context : Context) : DBHelper{
            if (INSATNCE == null){
                synchronized(this){
                    INSATNCE = Room.databaseBuilder(context,DBHelper::class.java,"weathr.db").fallbackToDestructiveMigration().allowMainThreadQueries().build()
                }
            }
            return INSATNCE!!
        }
    }
}