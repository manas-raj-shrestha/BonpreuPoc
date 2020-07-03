package com.dinube.bonpreu.data.saltedgedata.payment.paywithconnect.request

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("template_identifier")
                val templateIdentifier: String = "",
                @SerializedName("show_consent_confirmation")
                val showConsentConfirmation: Boolean = false,
                @SerializedName("payment_attributes")
                val paymentAttributes: PaymentAttributes,
                @SerializedName("return_to")
                val returnTo: String = "",
                @SerializedName("provider_code")
                val providerCode: String = "",
                @SerializedName("customer_id")
                val customerId: String = "")