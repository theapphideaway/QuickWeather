package com.theapphideaway.quickweather.Services

import com.theapphideaway.quickweather.Model.Current.Main
import com.theapphideaway.quickweather.Model.Current.WeatherResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherInterface {

    //https://api.openweathermap.org/data/2.5/weather?q=$city&units=imperial&appid=c6afdab60aa89481e297e0a4f19af055
    @GET("weather")
    fun getCityWeather(
        @Query("q") city: String,
        @Query("units") degreeType: String,
        @Query("appid") apiKey: String
    ) : Deferred<WeatherResponse>

    @GET("weather")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") degreeType: String,
        @Query("appid") apiKey: String
    ): Deferred<WeatherResponse>

}