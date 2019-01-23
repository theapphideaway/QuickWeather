package com.theapphideaway.quickweather.Services

import com.theapphideaway.quickweather.MainActivity
import com.theapphideaway.quickweather.Model.Temperatures
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.*

class WeatherService {

    var temperatures = Temperatures()
    var main:JSONObject? = null
    var currentTemp: Int? = null
    var tempMin: Int? = null
    var tempMax: Int? = null

    fun fetchJSONOkHttp(Url: String): Temperatures{
        println("Attempting to fetch JSON")
        val request = Request.Builder()
            .url(Url).build()
        val client = OkHttpClient()

        var previousTemp: String? = null
        var count = 0

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                val body = response.body()?.string()
                println(body)

                var reader = JSONObject(body)
                temperatures = Temperatures()
                count = 0

                println(temperatures.CurrentTemp)

                main = reader.getJSONObject("main")
                currentTemp = main!!.getInt("temp")
                tempMin = main!!.getInt("temp_min")
                tempMax = main!!.getInt("temp_max")

                temperatures.CurrentTemp = currentTemp.toString()
                temperatures.HighTemp = tempMax.toString()
                temperatures.LowTemp = tempMin.toString()

                println("New Temp = " + temperatures.CurrentTemp)

                count = 1
            }
        })
        while (count == 0)
        {
            Thread.sleep(100)
        }

        if (temperatures.CurrentTemp != null)
        {
            return temperatures
        }
        else
        {
            temperatures.CurrentTemp = "Something went wrong"
            return temperatures
        }

    }
}