package com.dinube.bonpreu.data.saltedgedata.accounts

import com.google.gson.annotations.SerializedName

data class Meta(@SerializedName("next_page")
                val nextPage: String = "",
                @SerializedName("next_id")
                val nextId: String = "")