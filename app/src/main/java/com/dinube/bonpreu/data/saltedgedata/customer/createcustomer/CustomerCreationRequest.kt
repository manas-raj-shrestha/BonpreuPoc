package com.dinube.bonpreu.data.saltedgedata.customer.createcustomer

import com.google.gson.annotations.SerializedName

data class CustomerCreationRequest(@SerializedName("data")
                                   val data: Data)