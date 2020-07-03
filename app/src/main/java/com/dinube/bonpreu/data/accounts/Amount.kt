package com.dinube.bonpreu.data.accounts

import com.google.gson.annotations.SerializedName

data class Amount(@SerializedName("amount")
                  val amount: String = "",
                  @SerializedName("currency")
                  val currency: String = "")