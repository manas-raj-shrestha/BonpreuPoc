package com.dinube.bonpreu.data.saltedgedata.transactions

import com.google.gson.annotations.SerializedName

data class UserTransactionResponse(@SerializedName("data")
                                   val data: List<DataItem>?,
                                   @SerializedName("meta")
                                   val meta: Meta)