package com.mohankrishna.swipeapp.model.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class ApiInstance {
    private val TIMEOUT_SECONDS = 180
    var okHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS.toLong(), TimeUnit.SECONDS)
        .build()
    val retrofit=Retrofit
        .Builder()
        .client(okHttpClient)
        .baseUrl("https://app.getswipe.in/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val apiService=retrofit.create(ApiInterface::class.java)
}