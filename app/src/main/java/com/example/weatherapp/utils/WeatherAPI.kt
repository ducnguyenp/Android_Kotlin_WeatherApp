package com.example.weatherapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.weatherapp.R
import com.example.weatherapp.network.WeatherService
import com.weatherapp.models.WeatherResponse
import retrofit.*
import java.text.SimpleDateFormat
import java.util.*


class WeatherAPI(var context: Context) {
    private lateinit var customProgressDialog: Dialog

    fun getLocationWeatherDetail(latitude: Double, longitude: Double) {
        if (Constants.isNetworkAvailable(context)) {
            var retrofit: Retrofit = Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()
            val service: WeatherService =
                retrofit.create(WeatherService::class.java)
            val listCall: Call<WeatherResponse> =
                service.getWeather(latitude, longitude, Constants.METRIC_UNIT, Constants.APP_ID)

            showProgressDialog()
            listCall.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(response: Response<WeatherResponse>?, retrofit: Retrofit?) {
                    if (response!!.isSuccess) {
                        val weatherList: WeatherResponse = response.body()
                        setupUI(weatherList)
                    } else {
                        when (response.code()) {
                            400 -> Log.i("Error with api", "400")
                            404 -> Log.i("Error with api", "404")
                            else -> Log.i("Error with api", "Something else")
                        }
                    }
                    cancelProgressDialog()
                }

                override fun onFailure(t: Throwable?) {
                    cancelProgressDialog()
                    Log.i("Error with api", t!!.message.toString())
                }

            })
        } else {
            Toast.makeText(context, "Device is not connected to wifi", Toast.LENGTH_LONG).show()
        }
    }

    private fun showProgressDialog() {
        customProgressDialog = Dialog(context)
        customProgressDialog.setContentView(R.layout.dialog_custom_progress)
        customProgressDialog.show()
    }

    private fun cancelProgressDialog() {
        customProgressDialog.dismiss()
    }

    private fun getUnit(value: String): String? {
        var unit = "°C"
        if ("US" == value || "LR" == value || "MM" == value) {
            unit = "°F"
        }
        return unit
    }

    private fun unixTime(timex: Long): String? {
        val date = Date(timex * 1000L)
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("HH:mm:ss")
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(date)
    }

    private fun getViewUI(id: Int): TextView {
        return (context as Activity).findViewById<View>(id) as TextView
    }

    private fun setupUI(weatherList: WeatherResponse) {
        for (z in weatherList.weather.indices) {
            val tvMain = getViewUI(R.id.tv_main)
            val tvMainDesc = getViewUI(R.id.tv_main_description)
            val tvTemp = getViewUI(R.id.tv_temp)
            val tvHum = getViewUI(R.id.tv_humidity)
            val tvMin = getViewUI(R.id.tv_min)
            val tvMax = getViewUI(R.id.tv_max)
            val tvSpeed = getViewUI(R.id.tv_speed)
            val tvName = getViewUI(R.id.tv_name)
            val tvCountry = getViewUI(R.id.tv_country)
            val tvSunriseTime = getViewUI(R.id.tv_sunrise_time)
            val tvSunsetTime = getViewUI(R.id.tv_sunset_time)
            val ivMain =  (context as Activity).findViewById<View>(R.id.iv_main) as ImageView
            tvMain.text = weatherList.weather[z].main
            tvMainDesc.text = weatherList.weather[z].description
            tvTemp.text = weatherList.main.temp.toString() + getUnit((context as Activity).application.resources.configuration.locales.toString())
            tvHum.text = weatherList.main.humidity.toString() + " per cent"
            tvMin.text = weatherList.main.temp_min.toString() + " min"
            tvMax.text = weatherList.main.temp_max.toString() + " max"
            tvSpeed.text = weatherList.wind.speed.toString()
            tvName.text = weatherList.name
            tvCountry.text = weatherList.sys.country
            tvSunriseTime.text = unixTime(weatherList.sys.sunrise.toLong())
            tvSunsetTime.text = unixTime(weatherList.sys.sunset.toLong())

            when (weatherList.weather[z].icon) {
                "01d" -> ivMain.setImageResource(R.drawable.sunny)
                "02d" -> ivMain.setImageResource(R.drawable.cloud)
                "03d" -> ivMain.setImageResource(R.drawable.cloud)
                "04d" -> ivMain.setImageResource(R.drawable.cloud)
                "04n" -> ivMain.setImageResource(R.drawable.cloud)
                "10d" -> ivMain.setImageResource(R.drawable.rain)
                "11d" -> ivMain.setImageResource(R.drawable.storm)
                "13d" -> ivMain.setImageResource(R.drawable.snowflake)
                "01n" -> ivMain.setImageResource(R.drawable.cloud)
                "02n" -> ivMain.setImageResource(R.drawable.cloud)
                "03n" -> ivMain.setImageResource(R.drawable.cloud)
                "10n" -> ivMain.setImageResource(R.drawable.cloud)
                "11n" -> ivMain.setImageResource(R.drawable.rain)
                "13n" -> ivMain.setImageResource(R.drawable.snowflake)
            }
        }
    }
}