package com.dinube.bonpreu.demo.payment

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import com.dinube.bonpreu.BaseActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.demo.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.payment_success_activity.*

class PaymentSuccessActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_success_activity)

        initializeToolbar()
        initializeImageViews()
    }

    private fun initializeImageViews() {
        var stream = assets?.open("dinube_logo.png");
        iv_dinube_logo.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()

        stream = assets?.open("payment_success_graphics.png");
        iv_payment_success_graphics.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()
    }

    private fun initializeToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Pagament efectuat correctament"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, DashboardActivity::class.java))
    }

}