package com.example.weatherapp.business

import com.example.weatherapp.business.model.Weather
import com.example.weatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface WeatherApi {

    @GET("data/2.5/onecall?")
    fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("exclude") exclude: String = "minutely",
        @Query("appid") appid: String = "f8f4c1eb92c75f7adf32de36015abf37",
        @Query("lang") lang: String = "en"
    ) : Observable<WeatherDataModel>


}