package com.dinube.bonpreu.saltedge

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.R
import kotlinx.android.synthetic.main.salt_edge_payment_selection.*

class SaltEdgePaymentSelectionActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.salt_edge_payment_selection)

        btn_pay_with_creds.setOnClickListener {
            navigateToPayWithCreds()
        }

        btn_pay_direct_api.setOnClickListener {
            navigateToPayWithDirectApi()
        }
    }

    private fun navigateToPayWithCreds() {
        val paymentSelectionIntent = Intent(this@SaltEdgePaymentSelectionActivity, SaltEdgeCredentialsPaymentActivity::class.java)
        paymentSelectionIntent.putExtra("customer_id",  intent.getStringExtra("customer_id"))
        startActivity(paymentSelectionIntent)
    }

    private fun navigateToPayWithDirectApi() {
        val paymentSelectionIntent = Intent(this@SaltEdgePaymentSelectionActivity, SaltEdgeDirectPayActivity::class.java)
        paymentSelectionIntent.putExtra("customer_id",  intent.getStringExtra("customer_id"))
        startActivity(paymentSelectionIntent)
    }

}