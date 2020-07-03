package com.dinube.bonpreu.data.saltedgedata.accounts

import com.google.gson.annotations.SerializedName

data class AccountsResponse(@SerializedName("data")
                            val data: List<DataItem>?,
                            @SerializedName("meta")
                            val meta: Meta)