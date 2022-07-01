package com.example.weatherapp.presenters

import com.example.weatherapp.view.MainView

class MainPresenter : BasePresenter<MainView>() {


    //todo будет переменная репозитории

    override fun enable() {

    }

    fun refresh(lat: String, lon: String) {
        viewState.setLoading(true)
        //todo будет обрпщение к репозиториям
    }
}