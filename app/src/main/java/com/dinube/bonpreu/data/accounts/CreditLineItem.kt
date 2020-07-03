package com.dinube.bonpreu.data.accounts

import com.google.gson.annotations.SerializedName

data class CreditLineItem(@SerializedName("amount")
                          val amount: Amount,
                          @SerializedName("type")
                          val type: String = "",
                          @SerializedName("included")
                          val included: Boolean = false)