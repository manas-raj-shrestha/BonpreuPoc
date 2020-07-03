package com.dinube.bonpreu.data.tspconnection.taskid

import com.dinube.bonpreu.data.tspconnection.taskid.Data
import com.dinube.bonpreu.data.tspconnection.taskid.Metadata
import com.google.gson.annotations.SerializedName

data class TspTaskIdResponse(val metadata: Metadata,
                             val data: Data,
                             @SerializedName("operation_id")val operationId: String = "")