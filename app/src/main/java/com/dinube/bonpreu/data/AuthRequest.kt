package com.dinube.bonpreu.data

import com.google.gson.annotations.SerializedName

data class AuthRequest(@SerializedName("grant_type")var grant_type: String )