package com.theapphideaway.quickweather.Services

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ForcastService {
    private val API_KEY = "6afdab60aa89481e297e0a4f19af055"
    private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private var mRetrofit: Retrofit? = null

    init {
//        val requestInterceptor = Interceptor{chain ->
//
//            val url = chain.request()
//                .url()
//                .newBuilder()
//                .addQueryParameter("appid", API_KEY)
//                .build()
//            val request = chain.request()
//                .newBuilder()
//                .url(url)
//                .build()
//
//            return@Interceptor chain.proceed(request)
//        }

        val okHttpClient = OkHttpClient.Builder()
            .build()

        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun getJSONApi(): ForcastInterface {
        return mRetrofit!!.create<ForcastInterface>(ForcastInterface::class.java)
    }
}