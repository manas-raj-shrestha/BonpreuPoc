package com.dinube.bonpreu.data.saltedgedata.connection

import com.google.gson.annotations.SerializedName

data class LastStage(
                     @SerializedName("updated_at")
                     val updatedAt: String = "",
                     @SerializedName("name")
                     val name: String = "",
                     @SerializedName("created_at")
                     val createdAt: String = "",
                     @SerializedName("id")
                     val id: String = "")