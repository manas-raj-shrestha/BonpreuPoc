package com.dinube.bonpreu.data.saltedgedata.payment.paywithdirectapi.request

import com.google.gson.annotations.SerializedName

data class DirectApiPaymentRequest(@SerializedName("data")
                                   val data: Data)