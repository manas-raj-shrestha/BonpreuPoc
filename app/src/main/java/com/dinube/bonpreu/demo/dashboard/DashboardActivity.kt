package com.dinube.bonpreu.demo.dashboard

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinube.bonpreu.R
import com.dinube.bonpreu.ServiceBuilder
import com.dinube.bonpreu.TransactionAdapter
import com.dinube.bonpreu.TransactionAdapterDemo
import com.dinube.bonpreu.data.afterbanks.PaymentInitiateResponse
import com.dinube.bonpreu.data.afterbanks.Transactions
import com.dinube.bonpreu.data.afterbanks.TransactionsResponse
import com.dinube.bonpreu.demo.payment.PaymentUskActivity
import com.dinube.bonpreu.retroInterface.BudCalls
import kotlinx.android.synthetic.main.demo_dashboard_activity.*
import kotlinx.android.synthetic.main.demo_dashboard_activity.rv_transactions
import kotlinx.android.synthetic.main.layout_dashboard_grid_options.*
import kotlinx.android.synthetic.main.legal_terms_activity.*
import kotlinx.android.synthetic.main.legal_terms_activity.toolbar
import kotlinx.android.synthetic.main.payment_initiate_activity.*
import kotlinx.android.synthetic.main.salt_edge_dashboard_activity.*
import kotlinx.android.synthetic.main.sign_up_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class DashboardActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_dashboard_activity)
        initializeToolbar()
        initializeImageViews()
        setUpRecyclerView()

        fab.setOnClickListener{startActivity(Intent(this@DashboardActivity, PaymentUskActivity::class.java))}
    }

    private fun setUpRecyclerView() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)

        var transactions: List<Transactions>?

        val sharedPref = getSharedPreferences(
            "dinube_pref", Context.MODE_PRIVATE)
        val token =
            sharedPref.getString("user_trusted_beneficiary_token", "NA")
        Log.e("Payment ini", token)
        val call = request.getTransactions("https://apipsd2.afterbanks.com/transactions/", "s2be1zyaihpmhgzy","07-01-2020",token!!,"A9DA3E6B009A588F5690A71AC18AA739EE5CB07E34D6012835E4B0D936850C21")

        call.enqueue(object : Callback<List<TransactionsResponse>> {
            override fun onResponse(call: Call<List<TransactionsResponse>>, response: Response<List<TransactionsResponse>>) =
                if (response.isSuccessful){
                    progress_bar1.visibility = View.GONE
                   transactions = response.body()?.get(0)?.transactions

                    rv_transactions.layoutManager = LinearLayoutManager(this@DashboardActivity)
                    rv_transactions.adapter = transactions?.let { TransactionAdapterDemo(it) }
                    rv_transactions.adapter!!.notifyDataSetChanged()

                }else{
                    Log.e("error", response.errorBody().toString())
                    Toast.makeText(this@DashboardActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            override fun onFailure(call: Call<List<TransactionsResponse>>, t: Throwable) {
                Log.e("error", t.message)
                Toast.makeText(this@DashboardActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })

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
}