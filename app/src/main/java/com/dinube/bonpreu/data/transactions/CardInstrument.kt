package com.dinube.bonpreu.data.transactions

data class CardInstrument(val identification: String = "",
                          val cardSchemeName: String = "",
                          val cardholderName: String = "",
                          val cardAuthorisationType: String = "")