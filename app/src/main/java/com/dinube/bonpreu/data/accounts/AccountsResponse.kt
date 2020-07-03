package com.dinube.bonpreu.data.accounts

import com.google.gson.annotations.SerializedName

data class AccountsResponse(@SerializedName("metadata")
                            val metadata: Metadata,
                            @SerializedName("data")
                            val data: List<DataItem>?,
                            @SerializedName("operation_id")
                            val operationId: String = "")