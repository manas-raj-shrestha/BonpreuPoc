package com.dinube.bonpreu.data

import com.google.gson.annotations.SerializedName

data class Authentication(@SerializedName("data") val authenticationData: AuthenticationData, @SerializedName("operation_id")
                          val operationId: String = "")