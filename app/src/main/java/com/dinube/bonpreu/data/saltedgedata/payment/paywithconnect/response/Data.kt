package com.dinube.bonpreu.data.saltedgedata.payment.paywithconnect.response

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("expires_at")
                val expiresAt: String = "",
                @SerializedName("connect_url")
                val connectUrl: String = "")