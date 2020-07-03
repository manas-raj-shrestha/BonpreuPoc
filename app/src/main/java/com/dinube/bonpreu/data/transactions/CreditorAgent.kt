package com.dinube.bonpreu.data.transactions

data class CreditorAgent(val schemeName: String = "",
                         val postalAddress: PostalAddress,
                         val identification: String = "",
                         val name: String = "")