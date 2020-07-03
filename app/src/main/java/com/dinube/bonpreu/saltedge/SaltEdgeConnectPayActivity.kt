package com.dinube.bonpreu.saltedge

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.ServiceBuilder
import com.dinube.bonpreu.data.saltedgedata.payment.paywithconnect.request.ConnectPayRequest
import com.dinube.bonpreu.data.saltedgedata.payment.paywithconnect.response.ConnectPayResponse
import com.dinube.bonpreu.data.saltedgedata.payment.paywithdirectapi.request.Data
import com.dinube.bonpreu.data.saltedgedata.payment.paywithdirectapi.request.DirectApiPaymentRequest
import com.dinube.bonpreu.data.saltedgedata.payment.paywithdirectapi.request.PaymentAttributes
import com.dinube.bonpreu.data.saltedgedata.payment.paywithdirectapi.response.DirectPayResponse
import com.dinube.bonpreu.retroInterface.BudCalls
import kotlinx.android.synthetic.main.salt_edge_direct_api_payment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class SaltEdgeConnectPayActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.salt_edge_direct_api_payment)

        btn_direct_pay.setOnClickListener {
            executeDirectPay()
            btn_direct_pay.text = "Preparing..."
            btn_direct_pay.visibility = View.GONE
        }
    }

    private fun executeDirectPay() {
        val request =
            ServiceBuilder.buildService(BudCalls::class.java)


//        val call = request.payWithConnect("https://www.saltedge.com/api/payments/v1/payments/oauth", ConnectPayRequest(data))

//        call.enqueue(object : Callback<ConnectPayResponse> {
//            override fun onResponse(call: Call<ConnectPayResponse>, response: Response<ConnectPayResponse>) {
//                if (response.isSuccessful){
//                    web_view.visibility = View.VISIBLE
//                    web_view.webViewClient = WebViewClient()
//                    web_view.settings.javaScriptEnabled = true
//                    web_view.settings.domStorageEnabled = true
//                    web_view.webViewClient = CustomWebViewDirectPayClient()
//
//                    web_view.loadUrl(response.body()!!.data.connectUrl)
//                }else{
//                    Toast.makeText(this@SaltEdgeConnectPayActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onFailure(call: Call<ConnectPayResponse>, t: Throwable) {
//                Log.e("Error","${t.message}" )
//                Toast.makeText(this@SaltEdgeConnectPayActivity, "${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    inner class CustomWebViewDirectPayClient : WebViewClient() {
        override fun onPageStarted(
            view: WebView,
            url: String,
            favicon: Bitmap?
        ) {
            super.onPageStarted(view, url, favicon)

            val formatterUrl = URL(url)
            val baseUrl: String = formatterUrl.protocol.toString() + "://" + formatterUrl.host

            val uri: Uri = Uri.parse(url)
            uri.getQueryParameter("payment_id")

            if(baseUrl.contentEquals("https://py.dinube.com")){
                view.stopLoading()
                btn_direct_pay.text = "Pay Again"
                btn_direct_pay.visibility = View.VISIBLE
                view.visibility = View.GONE

                Toast.makeText(this@SaltEdgeConnectPayActivity, "Payment initiated. Please check the status on SaltEdge dashboard", Toast.LENGTH_LONG).show()

            }
        }

    }


}