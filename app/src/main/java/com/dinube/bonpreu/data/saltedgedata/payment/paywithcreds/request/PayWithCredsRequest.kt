package com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.request

import com.google.gson.annotations.SerializedName

data class PayWithCredsRequest(@SerializedName("data")
                               val data: Data)