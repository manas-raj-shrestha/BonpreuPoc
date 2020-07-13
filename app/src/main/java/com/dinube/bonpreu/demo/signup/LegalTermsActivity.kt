package com.dinube.bonpreu.demo.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.R
import kotlinx.android.synthetic.main.legal_terms_activity.*

class LegalTermsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.legal_terms_activity)
        initializeToolbar()
        initiateClickListeners()
    }

    private fun initiateClickListeners() {
        btn_accept_legal.setOnClickListener {
            navigateToSignUpActivity()
        }
    }

    private fun navigateToSignUpActivity() {
        startActivity(Intent(this, SignUpActivity::class.java))
    }

    private fun initializeToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Informacio Legal"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}