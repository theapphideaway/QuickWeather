package com.theapphideaway.quickweather.Services

import com.theapphideaway.quickweather.Model.LocationDetails
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.util.*

class WeatherService {

    var locationDetails = LocationDetails()
    //var forcast = Forcast()
    var main: JSONObject? = null
    var currentTemp: Int? = null
    var tempMin: Int? = null
    var tempMax: Int? = null
    var city: String? = null

    fun getCurrentWeather(Url: String): LocationDetails {
        println("Attempting to fetch JSON")
        val request = Request.Builder()
            .url(Url).build()
        val client = OkHttpClient()

        var previousTemp: String? = null
        var count = 0

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
                count = 1
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

                println("HOLD UP THE OBJECT IS: " + main)

                locationDetails.CurrentTemp = currentTemp
                locationDetails.HighTemp = tempMax
                locationDetails.LowTemp = tempMin
                locationDetails.City = city

                println("New Temp = " + locationDetails.CurrentTemp)

                count = 1
            }
        })
        while (count == 0) {
            Thread.sleep(100)
        }

        if (locationDetails.CurrentTemp != null) {
            return locationDetails
        } else {
            locationDetails.CurrentTemp = 404
            return locationDetails
        }

    }
}

//    fun getForcast(Url: String): GroupedForcast{
//        val unixSeconds: Long? = 0
//        var count = 0
//
//        var obj: String? = null
//        var finalList = ArrayList<Forcast>()
//        var quickSave = ArrayList<Long>()
//
//        var groupedForcast = GroupedForcast()
//// convert seconds to milliseconds
//
//
//        println("Attempting to fetch JSON")
//        val request = Request.Builder()
//            .url(Url).build()
//        val client = OkHttpClient()
//
//        client.newCall(request).enqueue(object: Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                println("Failed to execute request")
//            }
//
//            override fun onResponse(call: Call, response: okhttp3.Response) {
//                val body = response.body()?.string()
//                println("Forcast: $body")
//
//                count = 0
//
//                var reader = JSONObject(body)
//                var myList = reader.getJSONArray("list")
//                obj = myList.toString()
//                println(myList)
//
//                (0..(myList.length()-1)).forEach { i ->
//                    var item = myList.getJSONObject(i)
//                    var day= item.getInt("dt").toLong()
//                    var sdf = java.text.SimpleDateFormat("EEEE")
//                    var dateFormat = Date(day * 1000)
//                    forcast.Day = sdf.format(dateFormat)
//
//                    var sdfDate = java.text.SimpleDateFormat("MMM dd")
//                    forcast.Date = sdfDate.format((dateFormat))
//
//                    var temp = item.getJSONObject("temp")
//                    forcast.Temp = temp.getInt("day")
//                    forcast.HighTemp = temp.getInt("max")
//                    forcast.LowTemp = temp.getInt("min")
//
//                    var weather = item.getJSONArray("weather")
//
//                    for(i in 0..weather.length() -1) {
//                        var weatherItem = weather.getJSONObject(i)
//                        forcast.Description = weatherItem.getString("description")
//                    }
//
//                    finalList.add(forcast)
//
//                    forcast = Forcast()
//                }
//
//                groupedForcast.forcasts = finalList
//                 count = 1
//            }
//        })

//
//        while (count == 0)
//        {
//            Thread.sleep(100)
//        }
//
//        println(obj)
//        println(finalList)
//        println(quickSave)
//
//        return groupedForcast
//
//
//
//    }
//}
//
//class GroupedForcast{
//    var forcasts: List<Forcast>? = null
//}