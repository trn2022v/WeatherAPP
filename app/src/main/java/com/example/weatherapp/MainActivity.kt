package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_city_name.text = "Minsk"
        main_date_name.text = "21 april"
        main_weather_conditional_icon.setImageResource(R.drawable.ic_sun)
        main_temp.text = "25"
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
}