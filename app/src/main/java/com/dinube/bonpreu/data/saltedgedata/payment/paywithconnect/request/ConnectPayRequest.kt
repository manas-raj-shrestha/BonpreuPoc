package com.dinube.bonpreu.data.saltedgedata.payment.paywithconnect.request

import com.google.gson.annotations.SerializedName

data class ConnectPayRequest(@SerializedName("data")
                             val data: Data)