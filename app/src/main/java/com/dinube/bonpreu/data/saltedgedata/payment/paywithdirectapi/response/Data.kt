package com.dinube.bonpreu.data.saltedgedata.payment.paywithdirectapi.response

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("expires_at")
                val expiresAt: String = "",
                @SerializedName("payment_id")
                val paymentId: String = "",
                @SerializedName("redirect_url")
                val redirectUrl: String = "")