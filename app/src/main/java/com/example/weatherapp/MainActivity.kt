package com.example.weatherapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.business.model.DailyWeatherModel
import com.example.weatherapp.business.model.HourlyWeatherModel
import com.example.weatherapp.business.model.WeatherDataModel
import com.example.weatherapp.presenters.MainPresenter
import com.example.weatherapp.view.*
import com.example.weatherapp.view.adapters.MainDailyListAdapter
import com.example.weatherapp.view.adapters.MainHourlyListAdapter
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.ktx.moxyPresenter
import java.lang.StringBuilder

const val TAG = "GEO_TEST"

class MainActivity : MvpAppCompatActivity(), MainView {


    private val mainPresenter by moxyPresenter { MainPresenter() }

    private val geoService by lazy { LocationServices.getFusedLocationProviderClient(this) }
    private val locationRequest by lazy { initLocationRequest() }
    private lateinit var mLocation: Location

@SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        main_date_tv.text = "21 april"
        main_weather_conditional_icon.setImageResource(R.drawable.ic_sun)
        main_temp.text = "25\u00b0"
        main_min_temp.text = "17"
        main_max_temp.text = "27"
        main_weather_img.setImageResource(R.mipmap.union3x)
        main_weather_conditional_description.text = "clear sky"
        main_pressure_mu_tv.text = "1654 hPa"
        main_humidity_mu_tv.text = "70%"
        main_wind_speed_mu_tv.text = "2 m/s"
        main_sunrise_mu_tv.text = "6:30"
        main_sunset_mu_tv.text = "22:24"
    }
    //------------------------------------ moxy code

    override fun displayLocation(data: String) {
        main_city_name.text = data
    }


    override fun displayCurrentData(data: WeatherDataModel) {
        data.apply {
            main_date_tv.text = current.dt.toDateFormatOf(DAY_FULL_MONTH_NAME)
            main_weather_conditional_icon.setImageResource(current.weather[0].icon.provideIcon())
            main_temp.text = StringBuilder().append(current.temp.toDegree()).append("°").toString()
            daily[0].temp.apply {
                main_min_temp.text = min.toDegree()
                main_max_temp.text = max.toDegree()
            }
            main_weather_img.setImageResource(R.mipmap.union3x)
            main_weather_conditional_description.text = current.weather[0].description
            main_pressure_mu_tv.text =
                StringBuilder().append(current.pressure.toString()).append(" hPa").toString()
            main_humidity_mu_tv.text = StringBuilder().append(current.humidity.toString()).append(" %").toString()
            main_wind_speed_mu_tv.text = StringBuilder().append(current.wind_speed.toString()).append(" m/s").toString()
            main_sunrise_mu_tv.text = current.sunrise.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)
            main_sunset_mu_tv.text = current.sunset.toDateFormatOf(HOUR_DOUBLE_DOT_MINUTE)

        }

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


    // ----------------- location code/ -----------------

}