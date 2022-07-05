package com.example.weatherapp.business.repos

import android.util.Log
import com.example.weatherapp.business.ApiProvider
import com.example.weatherapp.business.model.WeatherDataModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

const val TAG = "MAINREPO"

class MainRepository(api: ApiProvider) : BaseRepository<MainRepository.ServerResponse>(api) {

    fun reloadData(lat: String, lon: String) {
        Observable.zip(
            api.provideWeatherApi().getWeatherForecast(lat, lon),
            api.provideGeoCodeApi().getCityByCoord(lat, lon).map {
                it.asSequence()
                    .map { model ->
                        model.name
                    }
                    .toList()
                    .filterNotNull()
                    .first()
            }
        ) { weatherData, geoCode -> ServerResponse(geoCode, weatherData) }

            .subscribeOn(Schedulers.io())
            .doOnNext { /* todo тут будет добавление  обьектов в базу данных */ }
            /*  .onErrorResumeNext { todo тут будет извлечение  обьекта из базы данных  }*/
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                dataEmitter.onNext(it)
            }, {
                Log.d(TAG, "reloadData:  $it")
            })
    }


    data class ServerResponse(
        val cityName: String,
        val weatherData: WeatherDataModel,
        val error: Throwable? = null
    )

}