package com.dinube.bonpreu.data.saltedgedata.connection

import com.google.gson.annotations.SerializedName

data class DataResponse(@SerializedName("expires_at")
                        val expiresAt: String = "",
                        @SerializedName("connect_url")
                        val connectUrl: String = "")