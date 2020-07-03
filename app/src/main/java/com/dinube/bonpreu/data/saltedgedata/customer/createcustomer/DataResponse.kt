package com.dinube.bonpreu.data.saltedgedata.customer.createcustomer

import com.google.gson.annotations.SerializedName

data class DataResponse(@SerializedName("identifier")
                        val identifier: String = "",
                        @SerializedName("id")
                        val id: String = "",
                        @SerializedName("secret")
                        val secret: String = "")