package com.dinube.bonpreu.data.accounts

import com.google.gson.annotations.SerializedName

data class Metadata(@SerializedName("results")
                    val results: Int = 0)