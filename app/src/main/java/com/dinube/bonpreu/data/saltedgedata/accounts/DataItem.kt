package com.dinube.bonpreu.data.saltedgedata.accounts

import com.google.gson.annotations.SerializedName

data class DataItem(@SerializedName("balance")
                    val balance: Double = 0.0,
                    @SerializedName("updated_at")
                    val updatedAt: String = "",
                    @SerializedName("connection_id")
                    val connectionId: String = "",
                    @SerializedName("nature")
                    val nature: String = "",
                    @SerializedName("extra")
                    val extra: Extra,
                    @SerializedName("name")
                    val name: String = "",
                    @SerializedName("created_at")
                    val createdAt: String = "",
                    @SerializedName("id")
                    val id: String = "",
                    @SerializedName("currency_code")
                    val currencyCode: String = "")