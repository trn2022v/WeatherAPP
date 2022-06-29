package com.example.weatherapp

import android.app.TaskStackBuilder.create
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import android.media.MediaParser.create
import android.media.MediaPlayer.create
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.os.LocaleListCompat.create
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.business.model.WeatherData
import com.example.weatherapp.presenters.MainPresenter
import com.example.weatherapp.view.MainView
import com.example.weatherapp.view.adapters.MainDailyListAdapter
import com.example.weatherapp.view.adapters.MainHourlyListAdapter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.net.URI.create
import java.util.jar.Manifest
import kotlin.reflect.KProperty

const val GEO_LOCATION_REQUEST_COD_SUCCESS = 1
const val TAG = "GEO_TEST"

class MainActivity : MvpAppCompatActivity(), MainView {


    private val mainPresenter by moxyPresenter { MainPresenter() }

    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy { initLocationRequest() }
    private lateinit var mLocation: Location


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        checkPermission()
        initViews()

        main_hourly_list.apply {
            adapter = MainHourlyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)

        }
        main_daily_list.apply {
            adapter = MainDailyListAdapter()
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }

        mainPresenter.enable()

        geoService.requestLocationUpdates(locationRequest, geoCallBack, null)

    }

    private fun initViews() {
        main_city_name.text = "Minsk"
        main_date_name.text = "21 april"
        main_weather_conditional_icon.setImageResource(R.drawable.ic_sun)
        main_temp.text = "25\u00b0"
        main_min_temp.text = " 17"
        main_max_temp.text = "27"
        main_weather_img.setImageResource(R.mipmap.union3x)
        main_weather_conditional_description.text = "clear sky"
        main_pressure_mu_tv.text = "1654 hPa"
        main_humidity_mu_tv.text = "70%"
        main_wind_speed_mu_tv.text = "2 m/s"
        main_sunup_mu_tv.text = "6:30"
        main_sundown_mu_tv.text = "22:24"
    }
    //------------------------------------ moxy code

    override fun displayLocation(data: String) {
        main_city_name.text = data
    }

    override fun displayCurrentData(data: WeatherData) {
        main_city_name.text = "Minsk"
        main_date_name.text = "21 april"
        main_weather_conditional_icon.setImageResource(R.drawable.ic_sun)
        main_temp.text = "25\u00b0"
        main_min_temp.text = " 17"
        main_max_temp.text = "27"
        main_weather_img.setImageResource(R.mipmap.union3x)
        main_weather_conditional_description.text = "clear sky"
        main_pressure_mu_tv.text = "1654 hPa"
        main_humidity_mu_tv.text = "70%"
        main_wind_speed_mu_tv.text = "2 m/s"
        main_sunup_mu_tv.text = "6:30"
        main_sundown_mu_tv.text = "22:24"
    }

    override fun displayHourlyData(data: List<HourlyWeatherModel>) {
        (main_hourly_list.adapter as MainHourlyListAdapter).updateData(data)
    }

    override fun displayDailyData(data: List<DailyWeatherModel>) {
        (main_daily_list.adapter as MainDailyListAdapter).updateData(data)

    }

    override fun displayError(error: Throwable) {
    }

    override fun setLoading(flag: Boolean) {
    }

    //------------------------------------ moxy code/----


    // ----------------- location code -----------------

    private fun initLocationRequest(): com.google.android.gms.location.LocationRequest {
        val request = com.google.android.gms.location.LocationRequest.create()
        return request.apply {
            interval = 10000
            fastestInterval = 5000
            Priority.PRIORITY_HIGH_ACCURACY
        }

    }

    private val geoCallBack = object : LocationCallback() {
        override fun onLocationResult(geo: LocationResult) {
            Log.d(TAG, "onLocationResult: ${geo.locations.size}")
            for (location in geo.locations) {
                mLocation = location
                mainPresenter.refresh(mLocation.latitude.toString(), mLocation.longitude.toString())
                Log.d(
                    TAG,
                    "onLocationResult: let :${location.latitude}; lon :${location.longitude}"
                )
            }
        }
    }
    // ------ initial activity code -------

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "onRequestPermissionsResult: $requestCode")

        //TODO будет запуск main activity
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Нам нужны гео данные")
                .setMessage("Пожалуйста, разрешите доступ к гео данным дял продолжения пользвонания приложением")
                .setPositiveButton("ok") { _, _ ->
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        GEO_LOCATION_REQUEST_COD_SUCCESS
                    )
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),
                        GEO_LOCATION_REQUEST_COD_SUCCESS
                    )
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                .show()
        }

    }

    // ----------------- location code/ -----------------

}