package com.example.weatherapp.business

import com.example.weatherapp.business.model.GeoCodeModel
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoCodingApi {

    @GET("geo/1.0/reverse?")
    fun getCityByCoord(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("limit") limit: String = "5",
        @Query("appid") id: String = "bb05d5c931a1b4f1228180fe13221ed6"

    ): Observable<List<GeoCodeModel>>
}