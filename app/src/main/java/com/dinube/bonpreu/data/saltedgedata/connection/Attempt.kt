package com.dinube.bonpreu.data.saltedgedata.connection

import com.google.gson.annotations.SerializedName

data class Attempt(@SerializedName("return_to")
                   val returnTo: String = "")