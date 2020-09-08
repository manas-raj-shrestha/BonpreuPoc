package com.dinube.bonpreu.demo.dashboard

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinube.bonpreu.*
import com.dinube.bonpreu.data.afterbanks.*
import com.dinube.bonpreu.demo.bankconnection.consent.BankConsentActivity
import com.dinube.bonpreu.demo.payment.PaymentUskActivity
import com.dinube.bonpreu.retroInterface.BudCalls
import kotlinx.android.synthetic.main.bank_consent_activity.*
import kotlinx.android.synthetic.main.demo_dashboard_activity.*
import kotlinx.android.synthetic.main.demo_dashboard_activity.rv_transactions
import kotlinx.android.synthetic.main.demo_dashboard_activity.web_view_bank_consent
import kotlinx.android.synthetic.main.layout_dashboard_grid_options.*
import kotlinx.android.synthetic.main.legal_terms_activity.*
import kotlinx.android.synthetic.main.legal_terms_activity.toolbar
import kotlinx.android.synthetic.main.payment_initiate_activity.*
import kotlinx.android.synthetic.main.salt_edge_dashboard_activity.*
import kotlinx.android.synthetic.main.sign_up_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL
import kotlin.random.Random

class DashboardActivity: BaseActivity() {
    lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_dashboard_activity)

        val sharedPref = getSharedPreferences(
            "dinube_pref", Context.MODE_PRIVATE)
         token =
             sharedPref.getString("user_trusted_beneficiary_token", "NA").toString()

        initializeToolbar()
        initializeImageViews()
//        getTransactionList()
//        initializeRenew(token)

        fab.setOnClickListener{startActivity(Intent(this@DashboardActivity, PaymentUskActivity::class.java))}
    }

    private fun getTransactionList() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)

        var transactions: List<Transactions>?



        val call = request.getTransactions("https://apipsd2.afterbanks.com/transactions/", "s2be1zyaihpmhgzy","07-01-2020",token!!,"ES018202000000000000000000040780458")

        call.enqueue(object : Callback<List<TransactionsResponse>> {
            override fun onResponse(call: Call<List<TransactionsResponse>>, response: Response<List<TransactionsResponse>>) =
                if (response.isSuccessful){
                   transactions = response.body()?.get(0)?.transactions
                    if (transactions?.isNotEmpty()!!){
                        setupRecyclerView(transactions)
                    }else{
                        makeToast("Error Connecting Application")
                    }
                }else{
                    makeToast("Error Connecting Application")
                }
            override fun onFailure(call: Call<List<TransactionsResponse>>, t: Throwable) {
                makeToast("${t.message}")
            }
        })

    }

    fun setupRecyclerView(transactions: List<Transactions>?){
        rv_transactions.layoutManager = LinearLayoutManager(this@DashboardActivity)
        rv_transactions.adapter = transactions?.let { TransactionAdapterDemo(it) }
        rv_transactions.adapter!!.notifyDataSetChanged()
    }

//    private fun getDemoData(): ArrayList<TransactionAdapter.TransactionItems> {
//        val transactionItems: ArrayList<TransactionAdapter.TransactionItems> = ArrayList()
//
//        for (i in 1..20){
//            transactionItems.add(TransactionAdapter.TransactionItems(Random.nextInt(1,500).toString(), "",Random.nextInt(1,30).toString() + " Nov, 2019", "03AE56X"))
//        }
//
//        return transactionItems
//    }

    private fun initializeToolbar() {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initializeImageViews() {
        var stream = assets?.open("offer.jpg");
        iv_offer_banner.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()

        iv_piggy_bank.background.setColorFilter(Color.parseColor("#fcbd1b"), PorterDuff.Mode.SRC);
        iv_menu.background.setColorFilter(Color.parseColor("#1e5532"), PorterDuff.Mode.SRC);
        iv_bank.background.setColorFilter(Color.parseColor("#f70021"), PorterDuff.Mode.SRC);
        iv_transaction.background.setColorFilter(Color.parseColor("#a4a4a4"), PorterDuff.Mode.SRC);
    }

    private fun initializeRenew(token: String){
        val alertbox: AlertDialog? = this.let {
            AlertDialog.Builder(it)
                .setMessage("Your 90 days trusted beneficiary session has ended. Please click okay to renew session.")
                .setPositiveButton("Okay", DialogInterface.OnClickListener { arg0, arg1 ->
                    renewToken(token)

                })
//                .setNegativeButton("No", // do something when the button is clicked
//                    DialogInterface.OnClickListener { arg0, arg1 ->
//                    })
                .show()
        }
    }
    private fun renewToken(token: String){
        val request = ServiceBuilder.buildService(BudCalls::class.java)

        val call = request.getConsentRenew("https://apipsd2.afterbanks.com/consent/renew/", "s2be1zyaihpmhgzy",token)

        call.enqueue(object : Callback<ConsentRenew> {
            override fun onResponse(call: Call<ConsentRenew>, response: Response<ConsentRenew>) =
                if (response.isSuccessful){
                    web_view_bank_consent.loadUrl(response.body()?.follow)
                    web_view_bank_consent.visibility = View.VISIBLE
                    cl_parent.visibility = View.GONE
                    web_view_bank_consent.webViewClient = WebViewClient()
                    web_view_bank_consent.settings.javaScriptEnabled = true
                    web_view_bank_consent.settings.domStorageEnabled = true
                    web_view_bank_consent.webViewClient = CustomWebViewClientPayment()
                }else{
                    makeToast("Error Connecting Application")
                }
            override fun onFailure(call: Call<ConsentRenew>, t: Throwable) {
                makeToast("${t.message}")
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
                makeToast("Bank successfully added!! \n Session renewed")
                view.visibility = View.GONE
                cl_parent.visibility = View.VISIBLE
//                progress_bar.visibility = View.VISIBLE
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

//                    Log.e("token saved", sharedPref.getString("user_trusted_beneficiary_token", "NA"))

                }else{
                    Toast.makeText(this@DashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ConsentResponse>, t: Throwable) {
//                Log.e("error", t.message)
                Toast.makeText(this@DashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}