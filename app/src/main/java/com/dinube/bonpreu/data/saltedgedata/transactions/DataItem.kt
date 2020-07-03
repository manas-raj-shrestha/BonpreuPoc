package com.dinube.bonpreu.data.saltedgedata.transactions

import com.google.gson.annotations.SerializedName

data class DataItem(@SerializedName("amount")
                    val amount: Double = 0.0,
                    @SerializedName("description")
                    val description: String = "",
                    @SerializedName("created_at")
                    val createdAt: String = "",
                    @SerializedName("currency_code")
                    val currencyCode: String = "",
                    @SerializedName("duplicated")
                    val duplicated: Boolean = false,
                    @SerializedName("mode")
                    val mode: String = "",
                    @SerializedName("account_id")
                    val accountId: String = "",
                    @SerializedName("made_on")
                    val madeOn: String = "",
                    @SerializedName("updated_at")
                    val updatedAt: String = "",
                    @SerializedName("id")
                    val id: String = "",
                    @SerializedName("category")
                    val category: String = "",
                    @SerializedName("status")
                    val status: String = "")