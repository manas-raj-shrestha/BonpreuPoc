package com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.request

import com.google.gson.annotations.SerializedName

data class PaymentAttributes(@SerializedName("amount")
                             val amount: String = "100",
                             @SerializedName("creditor_country_code")
                             val creditorCountryCode: String = "UK",
                             @SerializedName("creditor_building_number")
                             val creditorBuildingNumber: String = "One",
                             @SerializedName("description")
                             val description: String = "Stocks purchase",
                             @SerializedName("creditor_post_code")
                             val creditorPostCode: String = "E14 5AB",
                             @SerializedName("creditor_town")
                             val creditorTown: String = "London",
                             @SerializedName("customer_last_logged_at")
                             val customerLastLoggedAt: String = "2018-11-21T13:48:40Z",
                             @SerializedName("currency_code")
                             val currencyCode: String = "EUR",
                             @SerializedName("customer_device_os")
                             val customerDeviceOs: String = "iOS 11",
                             @SerializedName("reference")
                             val reference: String = "p:1928384756",
                             @SerializedName("mode")
                             val mode: String = "normal",
                             @SerializedName("customer_ip_address")
                             val customerIpAddress: String = "10.0.0.1",
                             @SerializedName("creditor_street_name")
                             val creditorStreetName: String = "One Canada Square",
                             @SerializedName("end_to_end_id")
                             val endToEndId: String = "#123123123",
                             @SerializedName("creditor_name")
                             val creditorName: String = "Jay Dawson",
                             @SerializedName("creditor_iban")
                             val creditorIban: String = "GB33BUKB20201555555555")