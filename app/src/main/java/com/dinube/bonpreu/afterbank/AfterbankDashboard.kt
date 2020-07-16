package com.dinube.bonpreu.afterbank

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.DashboardActivity
import com.dinube.bonpreu.R
import kotlinx.android.synthetic.main.dashboard_activity.*
import retrofit2.Call
import retrofit2.Response
import java.net.URL

class AfterbankDashboard: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.afterbank_dashboard_activity)


//        val request = AfterbankAPIService.getAfterbankApi()
//        val call = request.getConsent()

        val call = AfterbankAPIService.getAfterbankApi().getConsent()
        call.enqueue(object:retrofit2.Callback<Consent> {
            override fun onResponse(
                call: Call<Consent>,
                response: Response<Consent>
            ) {
                if (response.isSuccessful){
                    btn_initiate_payment.visibility = View.GONE
                    web_view.visibility = View.VISIBLE
                    web_view.webViewClient = WebViewClient()
                    web_view.settings.javaScriptEnabled = true
                    web_view.settings.domStorageEnabled = true
                    web_view.webViewClient = CustomWebViewClientPayment()

                    web_view.loadUrl(response.body()!!.data.follow)

                    btn_connect_bank.visibility = View.GONE
                }else{
                    Toast.makeText(this@AfterbankDashboard, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Consent>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })

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
            Log.i("Listener", "Start" + baseUrl)

            if(baseUrl.contentEquals("https://py.dinube.com")){
                view.stopLoading()
                makeToast("Payment Successfully Initiated!!")
                view.visibility = View.GONE
                btn_initiate_payment.text = "Make A Payment"
                btn_initiate_payment.visibility = View.VISIBLE
                btn_connect_bank.visibility = View.VISIBLE
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Log.i("Listener", "Finish")
        }
    }
    fun makeToast(text: String){
        Toast.makeText(this@AfterbankDashboard, text, Toast.LENGTH_SHORT ).show()

    }
}