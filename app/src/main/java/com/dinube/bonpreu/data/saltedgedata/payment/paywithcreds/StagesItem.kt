package com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds

import com.google.gson.annotations.SerializedName

data class StagesItem(@SerializedName("name")
                      val name: String = "",
                      @SerializedName("created_at")
                      val createdAt: String = "",
                      @SerializedName("id")
                      val id: String = "")