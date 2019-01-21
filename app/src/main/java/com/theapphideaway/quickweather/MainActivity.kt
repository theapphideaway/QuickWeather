package com.theapphideaway.quickweather

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    var anotherWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?q=London&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fetchJSONOkHttp(anotherWeatherUrl)

    }

    fun fetchJSONOkHttp(Url: String){
        println("Attempting to fetch JSON")
        val request = Request.Builder()
            .url(Url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body()?.string()
                println(body)

                var reader = JSONObject(body)


                var main = reader.getJSONObject("main")
                var currentTemp = main.getInt("temp")
                var tempMin = main.getInt("temp_min")
                var tempMax = main.getInt("temp_max")

                println(currentTemp)
                println(tempMax)
                println(tempMin)

                runOnUiThread { temperature_text_view.text = currentTemp.toString() + "℉" }
            }
        })
    }
}
