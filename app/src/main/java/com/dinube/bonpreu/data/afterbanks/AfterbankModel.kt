package com.dinube.bonpreu.data.afterbanks

import com.google.gson.annotations.SerializedName

data class Consent(
    var data: ConsentData
)

data class ConsentResponse(
    var data: ConsentResponseData
)

data class ConsentData(
    var follow: String,
    var consentId: String
)

data class ConsentResponseData(
    var token: String,
    var consentId: String
//    var globalPosition: List<Users>
)

data class Users(
    @SerializedName("product") val product : String,
    @SerializedName("type") val type : String,
    @SerializedName("description") val description : String,
    @SerializedName("iban") val iban : String,
    @SerializedName("balance") val balance : Int,
    @SerializedName("currency") val currency : String
)

data class PaymentInitiateResponse(
    var follow: String,
    var paymentId: String
)

data class PaymentStatusResponse(
    var status: String
)

data class TransactionsResponse(
    @SerializedName("product") val product : String,
    @SerializedName("type") val type : String,
    @SerializedName("balance") val balance : Double,
    @SerializedName("countable_balance") val countable_balance : Int,
    @SerializedName("arranged_balance") val arranged_balance : Int,
    @SerializedName("balance_credit_granted") val balance_credit_granted : Int,
    @SerializedName("currency") val currency : String,
    @SerializedName("description") val description : String,
    @SerializedName("ownerName") val ownerName : String,
    @SerializedName("iban") val iban : String,
    @SerializedName("transactions") val transactions : List<Transactions>
)

data class Transactions(
    @SerializedName("date") val date : String,
    @SerializedName("date2") val date2 : String,
    @SerializedName("amount") val amount : Double,
    @SerializedName("balance") val balance : Double,
    @SerializedName("description") val description : String,
    @SerializedName("categoryId") val categoryId : Int,
    @SerializedName("transactionId") val transactionId : String
)