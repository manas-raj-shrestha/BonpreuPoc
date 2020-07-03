package com.dinube.bonpreu.data.saltedgedata.connection

import com.google.gson.annotations.SerializedName

data class FetchConnectionsResponse(@SerializedName("data")
                                    val data: List<FetchConnectionResponseData>?,
                                    @SerializedName("meta")
                                    val meta: Meta)

