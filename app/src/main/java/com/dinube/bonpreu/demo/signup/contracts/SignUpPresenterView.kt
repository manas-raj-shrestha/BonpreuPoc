package com.dinube.bonpreu.demo.signup.contracts

interface SignUpPresenterView {
    fun onSingularKeyError(error: String)
    fun onRegistrationSuccessful()
}