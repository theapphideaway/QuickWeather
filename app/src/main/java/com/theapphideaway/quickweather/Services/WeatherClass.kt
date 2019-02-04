package com.theapphideaway.quickweather.Services

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WeatherClass {
    private val API_KEY = "6afdab60aa89481e297e0a4f19af055"
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private var mRetrofit: Retrofit? = null

    init {

        val okHttpClient = OkHttpClient.Builder()
            .build()

        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getJSONApi(): WeatherInterface {
        return mRetrofit!!.create<WeatherInterface>(WeatherInterface::class.java!!)
    }
}