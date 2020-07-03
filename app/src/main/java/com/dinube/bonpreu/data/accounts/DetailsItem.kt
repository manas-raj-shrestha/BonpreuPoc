package com.dinube.bonpreu.data.accounts

import com.google.gson.annotations.SerializedName

data class DetailsItem(@SerializedName("scheme_name")
                       val schemeName: String = "",
                       @SerializedName("identification")
                       val identification: String = "",
                       @SerializedName("secondary_identification")
                       val secondaryIdentification: String = "",
                       @SerializedName("name")
                       val name: String = "")