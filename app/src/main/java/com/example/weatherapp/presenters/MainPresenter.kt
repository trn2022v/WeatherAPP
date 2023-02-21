package com.example.weatherapp.presenters

import android.annotation.SuppressLint
import android.util.Log
import com.example.weatherapp.business.ApiProvider
import com.example.weatherapp.business.repos.MainRepository
import com.example.weatherapp.view.MainView

class MainPresenter : BasePresenter<MainView>() {

    private val repo = MainRepository(ApiProvider())

    @SuppressLint("CheckResult")
    override fun enable() {
        repo.dataEmitter.subscribe { response ->
            Log.d("MAINREPO", "presenter enable: ")
            viewState.displayLocation(response.cityName)
            viewState.displayCurrentData(response.weatherData)
            viewState.displayDailyData(response.weatherData.daily)
            viewState.displayHourlyData(response.weatherData.hourly)
            response.error?.let{viewState.displayError(response.error)}
        }
    }

    fun refresh(lat: String, lon: String) {
        viewState.setLoading(true)
        repo.reloadData(lat,lon)
    }
}