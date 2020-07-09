package com.dinube.bonpreu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        btn_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))

        }

        btn_signup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

    }
}