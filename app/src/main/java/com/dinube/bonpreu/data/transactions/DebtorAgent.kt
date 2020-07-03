package com.dinube.bonpreu.data.transactions

data class DebtorAgent(val schemeName: String = "",
                       val postalAddress: PostalAddress,
                       val identification: String = "",
                       val name: String = "")