package com.dinube.bonpreu.data.accounts

import com.google.gson.annotations.SerializedName

data class DataItem(@SerializedName("account_type")
                    val accountType: String = "",
                    @SerializedName("balances")
                    val balances: List<BalancesItem>?,
                    @SerializedName("account_id")
                    val accountId: String = "",
                    @SerializedName("provider")
                    val provider: String = "",
                    @SerializedName("nickname")
                    val nickname: String = "",
                    @SerializedName("account_sub_type")
                    val accountSubType: String = "",
                    @SerializedName("currency")
                    val currency: String = "",
                    @SerializedName("details")
                    val details: List<DetailsItem>?)