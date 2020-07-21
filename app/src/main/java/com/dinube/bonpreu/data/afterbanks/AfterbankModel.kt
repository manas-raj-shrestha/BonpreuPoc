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