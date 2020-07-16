package com.dinube.bonpreu.demo.payment

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.BaseActivity
import com.dinube.bonpreu.R
import kotlinx.android.synthetic.main.legal_terms_activity.*
import kotlinx.android.synthetic.main.legal_terms_activity.toolbar
import kotlinx.android.synthetic.main.payment_initiation_activity.*
import kotlinx.android.synthetic.main.sign_up_activity.*

class PaymentUskActivity: BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_initiation_activity)
        initializeToolbar()
        initializeImageViews()
    }


    private fun initializeToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Realitzar pagament"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }


    private fun initializeImageViews() {
        var stream = assets?.open("dinube_logo.png");
        iv_dinube_logo.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()

         stream = assets?.open("tap_pay.png");
        iv_tap_pay.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()

        stream = assets?.open("barcode_sample.png");
        iv_barcode.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()
    }
}