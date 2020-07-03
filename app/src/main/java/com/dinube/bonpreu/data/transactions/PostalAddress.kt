package com.dinube.bonpreu.data.transactions

data class PostalAddress(val subDepartment: String = "",
                         val country: String = "",
                         val addressLine: List<String>?,
                         val addressType: String = "",
                         val townName: String = "",
                         val postcode: String = "",
                         val department: String = "",
                         val buildingNumber: String = "",
                         val streetName: String = "",
                         val countrySubdivision: String = "")