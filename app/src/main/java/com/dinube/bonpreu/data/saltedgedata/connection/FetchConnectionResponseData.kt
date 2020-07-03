package com.dinube.bonpreu.data.saltedgedata.connection

import com.google.gson.annotations.SerializedName


data class FetchConnectionResponseData(@SerializedName("last_success_at")
                                       val lastSuccessAt: String = "",
                                       @SerializedName("categorization")
                                       val categorization: String = "",
                                       @SerializedName("created_at")
                                       val createdAt: String = "",
                                       @SerializedName("secret")
                                       val secret: String = "",
                                       @SerializedName("last_attempt")
                                       val lastAttempt: LastAttempt,
                                       @SerializedName("country_code")
                                       val countryCode: String = "",
                                       @SerializedName("updated_at")
                                       val updatedAt: String = "",
                                       @SerializedName("daily_refresh")
                                       val dailyRefresh: Boolean = false,
                                       @SerializedName("show_consent_confirmation")
                                       val showConsentConfirmation: Boolean = false,
                                       @SerializedName("last_consent_id")
                                       val lastConsentId: String = "",
                                       @SerializedName("provider_id")
                                       val providerId: String = "",
                                       @SerializedName("id")
                                       val id: String = "",
                                       @SerializedName("provider_code")
                                       val providerCode: String = "",
                                       @SerializedName("next_refresh_possible_at")
                                       val nextRefreshPossibleAt: String = "",
                                       @SerializedName("store_credentials")
                                       val storeCredentials: Boolean = false,
                                       @SerializedName("provider_name")
                                       var providerName: String = "",
                                       @SerializedName("customer_id")
                                       val customerId: String = "",
                                       @SerializedName("status")
                                       val status: String = "")