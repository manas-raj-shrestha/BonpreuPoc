package com.dinube.bonpreu.retroInterface

import com.dinube.bonpreu.RP_SERVER_URL
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit

interface AfterBankApi {
        @POST("/consent/get")
        fun getConsent()

}

class AfterbankAPIService {

    companion object {
        public fun getAfterbankApi(): AfterBankApi {
            val cookieManager = CookieManager()
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

            val okHttpClientBuilder = OkHttpClient().newBuilder() //create OKHTTPClient
            okHttpClientBuilder.cookieJar(JavaNetCookieJar(cookieManager));

            val okHttpClient = okHttpClientBuilder
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build()

            val retrofit =
                Retrofit.Builder().client(okHttpClient)
                    .baseUrl("apipsd2.afterbanks.com/").build();

            return retrofit.create(AfterBankApi::class.java)
        }
    }

}