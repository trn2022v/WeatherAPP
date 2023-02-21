package com.example.weatherapp.business

import com.example.weatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface WeatherApi {

    @GET( "data/2.5/onecall?")
    fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") appid: String = "8c3ba12e2f41fc763541f7243ae82950",
        @Query("lang") lang: String = "en"
    ) : Observable<WeatherDataModel>
}