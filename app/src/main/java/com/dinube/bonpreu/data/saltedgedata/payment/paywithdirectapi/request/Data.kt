package com.dinube.bonpreu.data.saltedgedata.payment.paywithdirectapi.request

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("template_identifier")
                val templateIdentifier: String = "SEPA",
                @SerializedName("payment_attributes")
                val paymentAttributes: PaymentAttributes,
                @SerializedName("return_to")
                val returnTo: String = "https://py.dinube.com",
                @SerializedName("provider_code")
                val providerCode: String = "fake_oauth_client_xf",
                @SerializedName("customer_id")
                val customerId: String)