package com.dinube.bonpreu.data.transactions

data class CurrencyExchange(val unitCurrency: String = "",
                            val sourceCurrency: String = "",
                            val quotationDate: String = "",
                            val exchangeRate: String = "",
                            val instructedAmount: InstructedAmount,
                            val targetCurrency: String = "",
                            val contractIdentification: String = "")