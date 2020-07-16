package com.dinube.bonpreu.afterbank

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.*
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit


interface AfterBankApi {

    @GET("/consent/get")
    fun getConsent(): Call<Consent>

    @GET("/consent/response")
    fun getConsentResponse(): Call<ConsentResponse>

//    @FormUrlEncoded
//    @Headers("Content-Type: application/json","servicekey: s2be1zyaihpmhgzy","service: sandbox","grantType: payment","validUntil: 10-09-2020")
//    @POST("/consent/get")
//    fun getConsent(@Field("yourConsentCallback") yourConsentCallback: String): Call<ConsentResponse?>?

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
                    .baseUrl("https://nodejs-afterbank.herokuapp.com/").build();

            return retrofit.create(AfterBankApi::class.java)
        }
    }

}