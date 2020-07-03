package com.dinube.bonpreu.interseptor

import android.util.Log
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class BasicAuthInterceptor(user: String?, password: String?) :
    Interceptor {


    private val credentials: String = Credentials.basic("c3d29c4c-dd10-4e4e-83fc-4cf85005184a", "df1c748f6dec9c0798188b6d81d86b2943c9a0c737c7a802")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        Log.e("url", request.url.encodedPath)

        if(request.url.encodedPath.contentEquals("/v1/oauth/token")) {
            val authenticatedRequest: Request = request.newBuilder()
                .addHeader("Authorization", credentials)
                .addHeader("Content-Type", "application/x-www-form-urlencoded").build()
            return chain.proceed(authenticatedRequest)
        }

        else return chain.proceed(request)
    }

}