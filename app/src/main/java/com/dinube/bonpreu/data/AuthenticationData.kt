package com.dinube.bonpreu.data

import com.google.gson.annotations.SerializedName

data class AuthenticationData(@SerializedName("access_token")
                val accessToken: String = "",
                              @SerializedName("refresh_token")
                val refreshToken: String = "",
                              @SerializedName("token_type")
                val tokenType: String = "",
                              @SerializedName("expires_in")
                val expiresIn: Int = 0)