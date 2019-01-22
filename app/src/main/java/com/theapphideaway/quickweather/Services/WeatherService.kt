package com.theapphideaway.quickweather.Services

import com.theapphideaway.quickweather.MainActivity
import com.theapphideaway.quickweather.Model.Temperatures
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.*

class WeatherService {

    var temperatures = Temperatures()

    fun fetchJSONOkHttp(Url: String): Temperatures{
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

                temperatures.CurrentTemp = currentTemp.toString()
                temperatures.HighTemp = tempMax.toString()
                temperatures.LowTemp = tempMin.toString()

            }
        })

        while (temperatures.CurrentTemp == null)
        {
            Thread.sleep(100)
        }

        return temperatures
    }
}