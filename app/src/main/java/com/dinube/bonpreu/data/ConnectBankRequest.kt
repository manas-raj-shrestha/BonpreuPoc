package com.dinube.bonpreu.data

import com.google.gson.annotations.SerializedName

data class ConnectBankRequest(@SerializedName("redirect_url")var redirectUrl: String)