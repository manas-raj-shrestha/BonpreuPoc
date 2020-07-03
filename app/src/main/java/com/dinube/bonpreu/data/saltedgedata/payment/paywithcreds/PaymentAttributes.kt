package com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds

import com.google.gson.annotations.SerializedName

data class PaymentAttributes(@SerializedName("amount")
                             val amount: String = "",
                             @SerializedName("creditor_country_code")
                             val creditorCountryCode: String = "",
                             @SerializedName("creditor_building_number")
                             val creditorBuildingNumber: String = "",
                             @SerializedName("description")
                             val description: String = "",
                             @SerializedName("creditor_post_code")
                             val creditorPostCode: String = "",
                             @SerializedName("creditor_town")
                             val creditorTown: String = "",
                             @SerializedName("customer_last_logged_at")
                             val customerLastLoggedAt: String = "",
                             @SerializedName("currency_code")
                             val currencyCode: String = "",
                             @SerializedName("customer_device_os")
                             val customerDeviceOs: String = "",
                             @SerializedName("mode")
                             val mode: String = "",
                             @SerializedName("reference")
                             val reference: String = "",
                             @SerializedName("customer_ip_address")
                             val customerIpAddress: String = "",
                             @SerializedName("creditor_street_name")
                             val creditorStreetName: String = "",
                             @SerializedName("end_to_end_id")
                             val endToEndId: String = "",
                             @SerializedName("creditor_name")
                             val creditorName: String = "",
                             @SerializedName("creditor_iban")
                             val creditorIban: String = "")