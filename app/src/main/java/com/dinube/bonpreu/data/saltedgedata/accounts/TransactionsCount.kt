package com.dinube.bonpreu.data.saltedgedata.accounts

import com.google.gson.annotations.SerializedName

data class TransactionsCount(@SerializedName("pending")
                             val pending: Int = 0,
                             @SerializedName("posted")
                             val posted: Int = 0)