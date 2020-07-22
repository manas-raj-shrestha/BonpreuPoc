package com.dinube.bonpreu.demo.signup.contracts

import android.content.Intent

interface SignUpViewPresenter {

     fun fido2RegisterInitiate(username: String)
    fun onResult(requestCode: Int,resultCode: Int, data: Intent?)
}