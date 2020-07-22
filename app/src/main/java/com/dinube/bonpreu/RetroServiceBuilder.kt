package com.dinube.bonpreu


import android.util.Log
import com.dinube.bonpreu.interseptor.BasicAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
    private val client = OkHttpClient.Builder().addInterceptor(getHttp())     .addInterceptor { chain ->
        chain.proceed(chain.request())
    }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api-sandbox.thisisbud.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }

    fun   getHttp():HttpLoggingInterceptor{
        var http = HttpLoggingInterceptor()
        http.level = HttpLoggingInterceptor.Level.BODY
        http.level = HttpLoggingInterceptor.Level.HEADERS
        return http
    }
}