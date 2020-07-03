package com.dinube.bonpreu.data.transactions

import com.google.gson.annotations.SerializedName

data class DataItem(val enrichment: Enrichment,
                    @SerializedName("raw_transaction") val rawTransaction: RawTransaction,
                    val provider: String = "",
                    val id: String = "")