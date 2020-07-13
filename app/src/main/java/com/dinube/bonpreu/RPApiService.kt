package com.dinube.bonpreu

import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Header

interface RPApi {
    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("register/initiate")
    fun registerInitiate(@Body postBody: RequestBody): Call<ResponseBody>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("register/complete")
    fun registerComplete(@Header("Cookie") cookie: String, @Body body: RequestBody): Call<ResponseBody>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/initiate")
    fun authInitiate(@Body postBody: RequestBody): Call<ResponseBody>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("auth/complete")
    fun authComplete(@Header("Cookie") cookie: String, @Body postBody: RequestBody): Call<ResponseBody>

}

class RPApiService {

    companion object {
        public fun getApi(): RPApi {
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
                    .baseUrl(RP_SERVER_URL).build();

            return retrofit.create(RPApi::class.java)
        }
    }

}