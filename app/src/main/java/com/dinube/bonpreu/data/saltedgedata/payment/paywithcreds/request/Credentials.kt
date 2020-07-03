package com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.request

import com.google.gson.annotations.SerializedName

data class Credentials(@SerializedName("password")
                       val password: String = "",
                       @SerializedName("login")
                       val login: String = "")