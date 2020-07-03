package com.dinube.bonpreu.data.accounts

import com.google.gson.annotations.SerializedName

data class BalancesItem(@SerializedName("amount")
                        val amount: Amount,
                        @SerializedName("credit_line")
                        val creditLine: List<CreditLineItem>?,
                        @SerializedName("account_id")
                        val accountId: String = "",
                        @SerializedName("date_time")
                        val dateTime: String = "",
                        @SerializedName("type")
                        val type: String = "",
                        @SerializedName("credit_debit_indicator")
                        val creditDebitIndicator: String = "")