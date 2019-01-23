package com.theapphideaway.quickweather

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.theapphideaway.quickweather.Model.Temperatures
import com.theapphideaway.quickweather.Services.WeatherService

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    var Url = "https://api.openweathermap.org/data/2.5/weather?q=London&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"

    var weather = WeatherService()

    var temperatures = Temperatures()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        url_search_edit_text.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                var searchText = url_search_edit_text.text.toString()

                progressBar.visibility = View.VISIBLE

                Url = "https://api.openweathermap.org/data/2.5/weather?q=$searchText&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"

                //Temp isnt changing. Make sure ime options are enabled, check url, and make sure the data is being sent

                var newTemp =  weather.fetchJSONOkHttp(Url)

                println("new temp on main activity =" + newTemp.CurrentTemp)

                temperature_text_view.text = newTemp.CurrentTemp + "℉"
                return@OnKeyListener true

            }
            false
        })

        var newTemp =  weather.fetchJSONOkHttp(Url)
//
//
//
       temperature_text_view.text = newTemp.CurrentTemp + "℉"
    }



}
