package com.dinube.bonpreu

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinube.bonpreu.data.Authentication
import com.dinube.bonpreu.data.accounts.AccountsResponse
import com.dinube.bonpreu.data.transactions.TransactionResponse
import com.dinube.bonpreu.data.tspconnection.taskid.TspTaskIdResponse
import com.dinube.bonpreu.data.tspconnection.tspurl.TspUrlResponse
import com.dinube.bonpreu.data.tsppaymenturl.TspPaymentUrl
import com.dinube.bonpreu.retroInterface.BudCalls
import kotlinx.android.synthetic.main.dashboard_activity.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class DashboardActivity: AppCompatActivity(){

    lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.dashboard_activity)
        web_view.visibility = View.GONE

        setClickListener()

        val request = ServiceBuilder.buildService(BudCalls::class.java)
        val call = request.getAccessToken("application/x-www-form-urlencoded",("client_credentials"))

        call.enqueue(object : Callback<Authentication> {
            override fun onResponse(call: Call<Authentication>, response: Response<Authentication>) {

                if (response.isSuccessful){
                    accessToken = response.body()!!.authenticationData!!.accessToken
                }else{
                    Toast.makeText(this@DashboardActivity, "Error Authenticating Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Authentication>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

//        var transactions = ArrayList<TransactionAdapter.TransactionItems>()
//
//        for (i in 0..20){
//            transactions.add(TransactionAdapter.TransactionItems("a"+i,"b"+1))
//        }
//
//        rv_transactions.layoutManager = LinearLayoutManager(this@DashboardActivity)
//        rv_transactions.adapter = TransactionAdapter(transactions)
//
//        rv_accounts.layoutManager = LinearLayoutManager(this@DashboardActivity)
//        rv_accounts.adapter = TransactionAdapter(transactions)

    }

    private fun setClickListener() {
        btn_connect_bank.setOnClickListener {
            btn_connect_bank.text = "Preparing..."
            this.getTspTaskId()
        }

        btn_initiate_payment.setOnClickListener {
            btn_initiate_payment.text = "Preparing..."
            this.getTspPaymentUrl()

        }
    }

    private fun getTspPaymentUrl() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)
        val body: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "{\n    \"redirect_url\": \"https://py.dinube.com\",\n    \"provider\": \"NatwestSandbox\",\n    \"payment_details\": {\n        \"reference\": \"90990909090909\",\n        \"provider\": \"NatwestSandbox\",\n        \"recipient\": {\n            \"sort_code\": \"805401\",\n            \"name\": \"MR JOHN DOE\",\n            \"account_number\": \"22222123\"\n        },\n        \"amount\": {\n            \"value\": \"2.50\",\n            \"currency\": \"GBP\"\n        }\n    }\n}")


        val call = request.getTspPaymentUrl("Bearer "+accessToken, body )

        call.enqueue(object : Callback<TspPaymentUrl> {
            override fun onResponse(call: Call<TspPaymentUrl>, response: Response<TspPaymentUrl>) {

                if (response.isSuccessful){
                    btn_initiate_payment.visibility = View.GONE
                    web_view.visibility = View.VISIBLE
                    web_view.webViewClient = WebViewClient()
                    web_view.settings.javaScriptEnabled = true
                    web_view.settings.domStorageEnabled = true
                    web_view.webViewClient = CustomWebViewClientPayment()

                    web_view.loadUrl(response.body()!!.data.authorisationUrl)

                    btn_connect_bank.visibility = View.GONE
                }else{
                    Toast.makeText(this@DashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<TspPaymentUrl>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTspTaskId(){
        val request = ServiceBuilder.buildService(BudCalls::class.java)

        val body: RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), "{\n\"bank_name\": \"Natwest_Sandbox\",\n\"redirect_url\": \"https://py.dinube.com\"\n}")

        val call = request.getTspTaskId("Bearer "+accessToken, body )

        call.enqueue(object : Callback<TspTaskIdResponse> {
            override fun onResponse(call: Call<TspTaskIdResponse>, response: Response<TspTaskIdResponse>) {

                if (response.isSuccessful){
                    getTspUrl(response.body()!!.data.taskId)
                }else{
                    Toast.makeText(this@DashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<TspTaskIdResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getTspUrl(taskId: String){
        val request = ServiceBuilder.buildService(BudCalls::class.java)


        val call = request.getTspUrl("Bearer "+accessToken, taskId )

        call.enqueue(object : Callback<TspUrlResponse> {
            override fun onResponse(call: Call<TspUrlResponse>, response: Response<TspUrlResponse>) {

                if (response.isSuccessful){
                    web_view.visibility = View.VISIBLE
                    web_view.webViewClient = WebViewClient()
                    web_view.settings.javaScriptEnabled = true
                    web_view.settings.domStorageEnabled = true
                    web_view.webViewClient = CustomWebViewClient()

                    web_view.loadUrl(response.body()!!.data.url)

                    btn_connect_bank.visibility = View.GONE
                    btn_initiate_payment.visibility = View.GONE
                }else{
                    Toast.makeText(this@DashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<TspUrlResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

     fun makeToast(text: String){
        Toast.makeText(this@DashboardActivity, text, Toast.LENGTH_SHORT ).show()

    }

   inner class CustomWebViewClient : WebViewClient() {
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
                makeToast("Bank Successfully Added!!")
                view.visibility = View.GONE
                btn_connect_bank.visibility = View.VISIBLE
                btn_connect_bank.text = "Connect to Bank"
                btn_initiate_payment.visibility = View.VISIBLE

                getTransactions()
                getAccounts()
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Log.i("Listener", "Finish")
        }
    }

    private fun getTransactions() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)


        val call = request.getUserTransactions("Bearer "+accessToken )

        call.enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {

                if (response.isSuccessful){

                    var transactions = ArrayList<TransactionAdapter.TransactionItems>()
                    for(item in response.body()?.data!!){


                        transactions.add(TransactionAdapter.TransactionItems("Amount: " + item.rawTransaction.balance.amount.amount,
                               "Detail: " + item.rawTransaction.transactionInformation,"",""))

                        initializeRv(transactions)
                    }
                }else{
                    Toast.makeText(this@DashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getAccounts() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)


        val call = request.getUserAccounts("Bearer "+accessToken )

        call.enqueue(object : Callback<AccountsResponse> {
            override fun onResponse(call: Call<AccountsResponse>, response: Response<AccountsResponse>) {

                if (response.isSuccessful){

                    var transactions = ArrayList<TransactionAdapter.TransactionItems>()
                    for(item in response.body()?.data!!){

//
//                        transactions.add(TransactionAdapter.TransactionItems("Provider: "+ item.provider,
//                            "Balance: "+ item.balances?.get(0)?.amount!!.amount))

                        initializeAccountRv(transactions)
                    }
                }else{
                    Toast.makeText(this@DashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AccountsResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initializeRv(transactions: java.util.ArrayList<TransactionAdapter.TransactionItems>) {
        rv_transactions.layoutManager = LinearLayoutManager(this@DashboardActivity)
        rv_transactions.adapter = TransactionAdapter(transactions)


    }

    private fun initializeAccountRv(transactions: java.util.ArrayList<TransactionAdapter.TransactionItems>) {
        rv_accounts.layoutManager = LinearLayoutManager(this@DashboardActivity)
        rv_accounts.adapter = TransactionAdapter(transactions)
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
}