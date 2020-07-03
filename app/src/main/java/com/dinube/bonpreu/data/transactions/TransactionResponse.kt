package com.dinube.bonpreu.data.transactions

data class TransactionResponse(val metadata: Metadata,
                               val data: List<DataItem>?,
                               val operationId: String = "")