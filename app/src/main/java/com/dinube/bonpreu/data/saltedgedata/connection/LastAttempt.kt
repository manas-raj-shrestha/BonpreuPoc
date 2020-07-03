package com.dinube.bonpreu.data.saltedgedata.connection

import com.google.gson.annotations.SerializedName

data class LastAttempt(@SerializedName("finished_recent")
                       val finishedRecent: Boolean = false,
                       @SerializedName("interactive")
                       val interactive: Boolean = false,
                       @SerializedName("created_at")
                       val createdAt: String = "",
                       @SerializedName("device_type")
                       val deviceType: String = "",
                       @SerializedName("fetch_scopes")
                       val fetchScopes: List<String>?,
                       @SerializedName("locale")
                       val locale: String = "",
                       @SerializedName("automatic_fetch")
                       val automaticFetch: Boolean = false,
                       @SerializedName("api_mode")
                       val apiMode: String = "",

                       @SerializedName("remote_ip")
                       val remoteIp: String = "",
                       @SerializedName("daily_refresh")
                       val dailyRefresh: Boolean = false,
                       @SerializedName("to_date")
                       val toDate: String = "",
                       @SerializedName("updated_at")
                       val updatedAt: String = "",
                       @SerializedName("id")
                       val id: String = "",
                       @SerializedName("categorize")
                       val categorize: Boolean = false,
                       @SerializedName("user_agent")
                       val userAgent: String = "",

                       @SerializedName("last_stage")
                       val lastStage: LastStage,
                       @SerializedName("from_date")
                       val fromDate: String = "",
                       @SerializedName("finished")
                       val finished: Boolean = false,
                       @SerializedName("success_at")
                       val successAt: String = "",
                       @SerializedName("api_version")
                       val apiVersion: String = "",

                       @SerializedName("user_present")
                       val userPresent: Boolean = false,
                       @SerializedName("show_consent_confirmation")
                       val showConsentConfirmation: Boolean = false,
                       @SerializedName("store_credentials")
                       val storeCredentials: Boolean = false,
                       @SerializedName("partial")
                       val partial: Boolean = false,
                       @SerializedName("consent_id")
                       val consentId: String = "")