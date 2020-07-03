package com.dinube.bonpreu.data.tsppaymenturl

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("payment_id") val paymentId: String = "",
                @SerializedName("authorisation_url")val authorisationUrl: String = "")