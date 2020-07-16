package com.dinube.bonpreu.afterbank

data class Consent(
    var data: ConsentData
)

data class ConsentResponse(
    var data: ConsentResponseData
)

data class ConsentData(
    var follow: String,
    var consentId: String
)

data class ConsentResponseData(
    var consentId: String?
)