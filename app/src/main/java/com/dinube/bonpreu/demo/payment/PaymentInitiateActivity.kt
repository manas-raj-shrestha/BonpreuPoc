package com.dinube.bonpreu.demo.payment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.dinube.bonpreu.BaseActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.ServiceBuilder
import com.dinube.bonpreu.data.afterbanks.PaymentInitiateResponse
import com.dinube.bonpreu.data.afterbanks.PaymentStatusResponse
import com.dinube.bonpreu.demo.dashboard.DashboardActivity
import com.dinube.bonpreu.retroInterface.BudCalls
import kotlinx.android.synthetic.main.payment_initiate_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class PaymentInitiateActivity: BaseActivity() {

    lateinit var paymentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_initiate_activity)
        initiatePayment()
    }

    private fun initiatePayment() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)

        val sharedPref = getSharedPreferences(
            "dinube_pref", Context.MODE_PRIVATE)
        val token =
            sharedPref.getString("user_trusted_beneficiary_token", "NA")
        Log.e("Payment ini", token)
        val call = request.getPayment("https://apipsd2.afterbanks.com/payment/initiate/", "s2be1zyaihpmhgzy","normal","EUR","ABC","payment","https://nodejs-afterbank.herokuapp.com/payment/initiate/callback","ES8401826450000201500191","ES1801822200120201933578",token!!, 10.0)

        call.enqueue(object : Callback<PaymentInitiateResponse> {
            override fun onResponse(call: Call<PaymentInitiateResponse>, response: Response<PaymentInitiateResponse>) {

                if (response.isSuccessful){
                    progress_bar.visibility = View.GONE
                    paymentId = response.body()!!.paymentId
                    setUpWebView(response.body()!!.follow)
                }else{
                    Log.e("error", response.errorBody().toString())
                    Toast.makeText(this@PaymentInitiateActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<PaymentInitiateResponse>, t: Throwable) {
                Log.e("error", t.message)
                Toast.makeText(this@PaymentInitiateActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpWebView(url: String) {
        payment_web_view.loadUrl(url)
        payment_web_view.visibility = View.VISIBLE
        payment_web_view.webViewClient = WebViewClient()
        payment_web_view.settings.javaScriptEnabled = true
        payment_web_view.settings.domStorageEnabled = true
        payment_web_view.webViewClient = CustomWebViewClientPayment()
    }

    inner class CustomWebViewClientPayment : WebViewClient() {
        override fun onPageStarted(
            view: WebView,
            url: String,
            favicon: Bitmap?
        ) {
            super.onPageStarted(view, url, favicon)

            val formatterUrl = URL(url)
            val baseUrl: String = formatterUrl.protocol.toString() + "://" + formatterUrl.host
            Log.e("Listener", "Start" + url)

            if(url.contentEquals("https://apipsd2.afterbanks.com/callback/?callbackType=pagoConfirmado")){
                view.stopLoading()
                view.visibility = View.GONE
                progress_bar.visibility = View.VISIBLE
                getPaymentStatus()
            }
        }
    }

    private fun getPaymentStatus() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)

        val call = request.getPaymentStatus("https://apipsd2.afterbanks.com/payment/status/", paymentId,"s2be1zyaihpmhgzy")
        call.enqueue(object : Callback<PaymentStatusResponse> {
            override fun onResponse(call: Call<PaymentStatusResponse>, response: Response<PaymentStatusResponse>) {

                if (response.isSuccessful){
                    progress_bar.visibility = View.GONE
//                    Log.e("status", response.body()!!.status)
                    if(response.body()?.status == "RCVD" || response.body()?.status == "ACSC" || response.body()?.status == "ACCC"){
                        startActivity(Intent(this@PaymentInitiateActivity, PaymentSuccessActivity::class.java))
                        makeToast("Payment Successfully Initiated!!")
                    }
                    else if(response.body()?.status == "RJCT" || response.body()?.status == "CANC"){
                        startActivity(Intent(this@PaymentInitiateActivity, DashboardActivity::class.java))
                        makeToast("Payment has been canceled")

                    }
                }else{
                    Log.e("error", response.errorBody().toString())
                    Toast.makeText(this@PaymentInitiateActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<PaymentStatusResponse>, t: Throwable) {
                Log.e("error", t.message)
                Toast.makeText(this@PaymentInitiateActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}