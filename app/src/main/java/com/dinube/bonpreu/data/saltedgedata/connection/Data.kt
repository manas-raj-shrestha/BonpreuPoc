package com.dinube.bonpreu.data.saltedgedata.connection

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("customer_id")
                val customerId: String = "",
                @SerializedName("consent")
                val consent: Consent,
                @SerializedName("attempt")
                val attempt: Attempt)