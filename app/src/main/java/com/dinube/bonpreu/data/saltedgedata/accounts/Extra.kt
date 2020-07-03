package com.dinube.bonpreu.data.saltedgedata.accounts

import com.google.gson.annotations.SerializedName

data class Extra(@SerializedName("account_number")
                 val accountNumber: String = "",
                 @SerializedName("transactions_count")
                 val transactionsCount: TransactionsCount,
                 @SerializedName("current_date")
                 val currentDate: String = "",
                 @SerializedName("iban")
                 val iban: String = "",
                 @SerializedName("account_name")
                 val accountName: String = "",
                 @SerializedName("card_type")
                 val cardType: String = "",
                 @SerializedName("sort_code")
                 val sortCode: String = "",
                 @SerializedName("client_name")
                 val clientName: String = "",
                 @SerializedName("current_time")
                 val currentTime: String = "",
                 @SerializedName("swift")
                 val swift: String = "",
                 @SerializedName("status")
                 val status: String = "",
                 @SerializedName("last_posted_transaction_id")
                 val lastPostedTransactionId: String = "")