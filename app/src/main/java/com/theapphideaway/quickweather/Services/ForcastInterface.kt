package com.theapphideaway.quickweather.Services

import com.theapphideaway.quickweather.Model.Current.WeatherResponse
import com.theapphideaway.quickweather.Model.Forcast.ForcastResponse
import com.theapphideaway.quickweather.Model.Forcast.X
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ForcastInterface {

    @GET("forecast/daily")
    fun getForcast(
        @Query("q") city: String,
        @Query("cnt") count: Int,
        @Query("units") units: String,
        @Query("appid") apiKey: String
    ) : Deferred<ForcastResponse>
}