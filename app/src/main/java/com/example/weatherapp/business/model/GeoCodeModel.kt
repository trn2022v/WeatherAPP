package com.example.weatherapp.business.model

import com.example.weatherapp.business.model.LocalNames

data class GeoCodeModel(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String,
    val state: String,
    var isFavorite: Boolean = false   //todo будет применяться при добавлении в избранное в menu Activity
)