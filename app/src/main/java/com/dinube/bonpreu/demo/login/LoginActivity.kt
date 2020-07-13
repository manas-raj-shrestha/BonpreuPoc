package com.dinube.bonpreu.demo.login

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.demo.signup.SignUpActivity
import kotlinx.android.synthetic.main.legal_terms_activity.*
import kotlinx.android.synthetic.main.legal_terms_activity.toolbar
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.android.synthetic.main.login_activity.iv_fido_icon
import kotlinx.android.synthetic.main.login_activity.iv_singular_key_icon
import kotlinx.android.synthetic.main.sign_up_activity.*

class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        initializeToolbar()
        setRegisterAction()
        initializeImageViews()
    }

    private fun initializeImageViews() {
        var stream = assets?.open("fido_icon.png");
        iv_fido_icon.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()

        stream = assets?.open("singular_key_icon.png");
        iv_singular_key_icon.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()
    }

    private fun setRegisterAction() {
        tv_register.setOnClickListener { startActivity(Intent(this, SignUpActivity::class.java)) }
    }

    private fun initializeToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Iniciar Sessió"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}