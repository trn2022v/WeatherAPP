package com.example.weatherapp.business.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherDataEntity::class], exportSchema = false, version = 1)
abstract class OpenWeatherDatabase: RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDao

}