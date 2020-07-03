package com.dinube.bonpreu.data.transactions

import com.google.gson.annotations.SerializedName

data class RawTransaction(val transactionId: String = "",
                          val amount: Amount,
                          val addressLine: String = "",
                          val merchantDetails: MerchantDetails,
                          val bookingDateTime: String = "",
                          val statementReference: List<String>?,
                          val proprietaryBankTransactionCode: ProprietaryBankTransactionCode,
                          val creditDebitIndicator: String = "",
                          val debtorAccount: DebtorAccount,
                          val currencyExchange: CurrencyExchange,
                          val supplementaryData: SupplementaryData,
                          val accountId: String = "",
                          @SerializedName("transaction_information")  val transactionInformation: String = "",
                          val balance: Balance,
                          val creditorAccount: CreditorAccount,
                          val debtorAgent: DebtorAgent,
                          val valueDateTime: String = "",
                          val bankTransactionCode: BankTransactionCode,
                          val creditorAgent: CreditorAgent,
                          val cardInstrument: CardInstrument,
                          val transactionReference: String = "",
                          val status: String = "",
                          val chargeAmount: ChargeAmount)