package com.dinube.bonpreu.demo.bankconnection.consent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.dinube.bonpreu.data.afterbanks.Consent
import com.dinube.bonpreu.data.afterbanks.ConsentResponse
import com.dinube.bonpreu.demo.dashboard.DashboardActivity
import com.dinube.bonpreu.retroInterface.BudCalls
import kotlinx.android.synthetic.main.bank_consent_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL

class BankConsentActivity: BaseActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bank_consent_activity)

        initiateConsentGetCall()
    }

    private fun initiateConsentGetCall() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)

        val call = request.getConsent("https://nodejs-afterbank.herokuapp.com/consent/get")

        call.enqueue(object : Callback<Consent> {
            override fun onResponse(call: Call<Consent>, response: Response<Consent>) {

                if (response.isSuccessful){
                    web_view_bank_consent.loadUrl(response.body()?.data?.follow)
                    web_view_bank_consent.visibility = View.VISIBLE
                    web_view_bank_consent.webViewClient = WebViewClient()
                    web_view_bank_consent.settings.javaScriptEnabled = true
                    web_view_bank_consent.settings.domStorageEnabled = true
                    web_view_bank_consent.webViewClient = CustomWebViewClientPayment()
                }else{
                    Toast.makeText(this@BankConsentActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Consent>, t: Throwable) {
                Toast.makeText(this@BankConsentActivity, "${t.message}", Toast.LENGTH_SHORT).show()
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
            Log.e("Listener", "Start" + url)

            if(url.contentEquals("https://nodejs-afterbank.herokuapp.com/consent/response?statusAB=403")){
                view.stopLoading()
                makeToast("Bank successfully added!!")
                view.visibility = View.GONE
                progress_bar.visibility = View.VISIBLE
                getLastConsentToken()
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Log.i("Listener", "Finish")
        }
    }

    private fun getLastConsentToken() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)

        val call = request.getConsentResponse("https://nodejs-afterbank.herokuapp.com/consent/response")

        call.enqueue(object : Callback<ConsentResponse> {
            override fun onResponse(call: Call<ConsentResponse>, response: Response<ConsentResponse>) {

                if (response.isSuccessful){
                    val sharedPref = getSharedPreferences(
                        "dinube_pref", Context.MODE_PRIVATE)

                    with (sharedPref.edit()) {
                        putString("user_trusted_beneficiary_token", response.body()!!.data.token)
                        commit()
                    }

                    Log.e("token saved", sharedPref.getString("user_trusted_beneficiary_token", "NA"))

                    startActivity(Intent(this@BankConsentActivity, DashboardActivity::class.java))
                }else{
                    Toast.makeText(this@BankConsentActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ConsentResponse>, t: Throwable) {
                Log.e("error", t.message)
                Toast.makeText(this@BankConsentActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}