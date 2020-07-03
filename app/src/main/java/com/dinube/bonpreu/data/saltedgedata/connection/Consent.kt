package com.dinube.bonpreu.data.saltedgedata.connection

import com.google.gson.annotations.SerializedName

data class Consent(@SerializedName("from_date")
                   val fromDate: String = "",
                   @SerializedName("scopes")
                   val scopes: List<String>?)