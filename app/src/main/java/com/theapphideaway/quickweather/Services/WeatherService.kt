package com.theapphideaway.quickweather.Services

import com.theapphideaway.quickweather.Model.LocationDetails
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

class WeatherService {

    var locationDetails = LocationDetails()
    var main:JSONObject? = null
    var currentTemp: Int? = null
    var tempMin: Int? = null
    var tempMax: Int? = null
    var city: String? = null

    fun fetchJSONOkHttp(Url: String): LocationDetails{
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
                locationDetails = LocationDetails()
                count = 0

                println(locationDetails.CurrentTemp)

                main = reader.getJSONObject("main")
                currentTemp = main!!.getInt("temp")
                tempMin = main!!.getInt("temp_min")
                tempMax = main!!.getInt("temp_max")
                city = reader.getString("name")

                locationDetails.CurrentTemp = currentTemp
                locationDetails.HighTemp = tempMax
                locationDetails.LowTemp = tempMin
                locationDetails.City = city

                println("New Temp = " + locationDetails.CurrentTemp)

                count = 1
            }
        })
        while (count == 0)
        {
            Thread.sleep(100)
        }

        if (locationDetails.CurrentTemp != null)
        {
            return locationDetails
        }
        else
        {
            locationDetails.CurrentTemp = 404
            return locationDetails
        }

    }
}