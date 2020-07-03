package com.dinube.bonpreu.data.saltedgedata.transactions

import com.google.gson.annotations.SerializedName

data class Extra(@SerializedName("original_amount")
                 val originalAmount: Double = 0.0,
                 @SerializedName("original_currency_code")
                 val originalCurrencyCode: String = "",
                 @SerializedName("account_balance_snapshot")
                 val categorizationConfidence: Int = 0,
                 @SerializedName("merchant_id")
                 val merchantId: String = "")