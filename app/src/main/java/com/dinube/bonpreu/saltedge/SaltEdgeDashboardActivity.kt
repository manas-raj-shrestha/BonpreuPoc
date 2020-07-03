package com.dinube.bonpreu.saltedge

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinube.bonpreu.*
import com.dinube.bonpreu.data.saltedgedata.accounts.AccountsResponse
import com.dinube.bonpreu.data.saltedgedata.connection.*
import com.dinube.bonpreu.data.saltedgedata.transactions.UserTransactionResponse
import com.dinube.bonpreu.retroInterface.BudCalls
import kotlinx.android.synthetic.main.salt_edge_dashboard_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class SaltEdgeDashboardActivity: AppCompatActivity(){

    lateinit var accessToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.salt_edge_dashboard_activity)
        web_view.visibility = View.GONE

        setClickListener()

    }

    private fun setClickListener() {
        btn_connect_bank.setOnClickListener {
            btn_connect_bank.text = "Preparing..."
            this.getConnectionUrl()
        }

        btn_initiate_payment.setOnClickListener {
        navigateToPaymentModeSelectionActivity()
        }
    }

    private fun navigateToPaymentModeSelectionActivity() {
        val paymentSelectionIntent = Intent(this@SaltEdgeDashboardActivity, SaltEdgePaymentSelectionActivity::class.java)
        paymentSelectionIntent.putExtra("customer_id",  intent.getStringExtra("customer_id"))
        startActivity(paymentSelectionIntent)
    }

    private fun getConnectionUrl(){
        val request =
            ServiceBuilder.buildService(BudCalls::class.java)

       var body = ConnectionUrlRequest(Data(intent.getStringExtra("customer_id"), Consent("2020-01-01",
           arrayListOf("account_details","transactions_details")),Attempt("https://py.dinube.com")))

        val call = request.getConnectionUrl("https://www.saltedge.com/api/v5/connect_sessions/create",body  )

        call.enqueue(object : Callback<ConnectionUrlResponse> {
            override fun onResponse(call: Call<ConnectionUrlResponse>, response: Response<ConnectionUrlResponse>) {

                if (response.isSuccessful){
                    btn_initiate_payment.visibility = View.GONE
                    web_view.visibility = View.VISIBLE
                    web_view.webViewClient = WebViewClient()
                    web_view.settings.javaScriptEnabled = true
                    web_view.settings.domStorageEnabled = true
                    web_view.webViewClient = CustomWebViewClient()

                    web_view.loadUrl(response.body()!!.data.connectUrl)

                    btn_connect_bank.visibility = View.GONE
                }else{
                    Toast.makeText(this@SaltEdgeDashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ConnectionUrlResponse>, t: Throwable) {
                Toast.makeText(this@SaltEdgeDashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

     fun makeToast(text: String){
        Toast.makeText(this@SaltEdgeDashboardActivity, text, Toast.LENGTH_SHORT ).show()

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

                getConnections()
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            Log.i("Listener", "Finish")
        }
    }

    var userConnections = ArrayList<FetchConnectionResponseData>()

    private fun getConnections() {
        val request =
            ServiceBuilder.buildService(BudCalls::class.java)


        val call = request.fetchConnections("https://www.saltedge.com/api/v5/connections", intent.getStringExtra("customer_id") )

        call.enqueue(object : Callback<FetchConnectionsResponse> {
            override fun onResponse(call: Call<FetchConnectionsResponse>, response: Response<FetchConnectionsResponse>) {
                userConnections =  (response.body()?.data as ArrayList<FetchConnectionResponseData>?)!!
                if (response.isSuccessful){

                    var connections= ArrayList<String>()
                    for(item in response.body()?.data!!){
                       connections.add(item.providerName)
                    }

                    navigateToSelectionActivity(connections)
                }else{
                    Toast.makeText(this@SaltEdgeDashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<FetchConnectionsResponse>, t: Throwable) {
                Toast.makeText(this@SaltEdgeDashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToSelectionActivity(connections: ArrayList<String>) {
        var intent = Intent(this@SaltEdgeDashboardActivity, SelectionActivity::class.java)
        intent.putExtra("connections", connections)
        startActivityForResult(intent, 200)
    }

    private fun initializeRv(transactions: java.util.ArrayList<TransactionAdapter.TransactionItems>) {
        rv_transactions.layoutManager = LinearLayoutManager(this@SaltEdgeDashboardActivity)
        rv_transactions.adapter =
            TransactionAdapter(transactions)


    }

    private fun initializeAccountRv(transactions: java.util.ArrayList<TransactionAdapter.TransactionItems>) {
        rv_accounts.layoutManager = LinearLayoutManager(this@SaltEdgeDashboardActivity)
        rv_accounts.adapter = TransactionAdapter(transactions)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("pos",data!!.getIntExtra("connection_position",-1).toString())

        getUserAccounts(data!!.getIntExtra("connection_position",-1))
    }

    private fun getUserTransactions(position: Int, accountId: String) {
        val request =
            ServiceBuilder.buildService(BudCalls::class.java)

        val call = request.fetchSaltEdgeTransactions("https://www.saltedge.com/api/v5/transactions", accountId,userConnections[position].id)

        call.enqueue(object : Callback<UserTransactionResponse> {
            override fun onResponse(call: Call<UserTransactionResponse>, response: Response<UserTransactionResponse>) {
                if (response.isSuccessful){

                    var transactions = ArrayList<TransactionAdapter.TransactionItems>()
                    for(item in response.body()?.data!!){


                        transactions.add(
                            TransactionAdapter.TransactionItems(
                                "Amount: " + item.amount,
                                "Detail: " + item.description
                            )
                        )


                    }

                    initializeRv(transactions)

                }else{

                    Toast.makeText(this@SaltEdgeDashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<UserTransactionResponse>, t: Throwable) {
                Log.e("Error","${t.message}" )
                Toast.makeText(this@SaltEdgeDashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUserAccounts(position: Int) {
        val request =
            ServiceBuilder.buildService(BudCalls::class.java)


        val call = request.fetchSaltEdgeAccounts("https://www.saltedge.com/api/v5/accounts", userConnections[position].id)

        call.enqueue(object : Callback<AccountsResponse> {
            override fun onResponse(call: Call<AccountsResponse>, response: Response<AccountsResponse>) {
                if (response.isSuccessful){

                    var accounts = ArrayList<TransactionAdapter.TransactionItems>()
                    for(item in response.body()?.data!!){

                        accounts.add(
                            TransactionAdapter.TransactionItems(
                                "Provider: " + item.name,
                                "Balance: " + item.balance
                            )
                        )

                    }


                    initializeAccountRv(accounts)
                    getUserTransactions(position, response.body()?.data!![0].id)

                }else{
                    Toast.makeText(this@SaltEdgeDashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<AccountsResponse>, t: Throwable) {
                Toast.makeText(this@SaltEdgeDashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}