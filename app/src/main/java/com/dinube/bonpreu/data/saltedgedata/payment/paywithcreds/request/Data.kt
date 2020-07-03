package com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.request

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("template_identifier")
                val templateIdentifier: String = "SEPA",
                @SerializedName("show_consent_confirmation")
                val showConsentConfirmation: Boolean = false,
                @SerializedName("credentials")
                val credentials: Credentials,
                @SerializedName("payment_attributes")
                val paymentAttributes: PaymentAttributes,
                @SerializedName("provider_code")
                val providerCode: String = "fake_client_xf",
                @SerializedName("customer_id")
                val customerId: String = "")