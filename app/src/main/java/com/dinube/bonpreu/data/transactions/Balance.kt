package com.dinube.bonpreu.data.transactions

data class Balance(val amount: Amount,
                   val type: String = "",
                   val creditDebitIndicator: String = "")