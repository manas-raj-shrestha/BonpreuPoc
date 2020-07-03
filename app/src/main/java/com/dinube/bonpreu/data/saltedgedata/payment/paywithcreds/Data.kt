package com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds

import com.google.gson.annotations.SerializedName

data class Data(@SerializedName("template_identifier")
                val templateIdentifier: String = "",
                @SerializedName("updated_at")
                val updatedAt: String = "",
                @SerializedName("payment_attributes")
                val paymentAttributes: PaymentAttributes,
                @SerializedName("stages")
                val stages: List<StagesItem>?,
                @SerializedName("created_at")
                val createdAt: String = "",
                @SerializedName("id")
                val id: String = "",
                @SerializedName("provider_code")
                val providerCode: String = "",
                @SerializedName("provider_name")
                val providerName: String = "",
                @SerializedName("customer_id")
                val customerId: String = "",
                @SerializedName("status")
                val status: String = "")