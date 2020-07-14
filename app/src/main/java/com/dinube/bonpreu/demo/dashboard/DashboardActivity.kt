package com.dinube.bonpreu.demo.dashboard

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinube.bonpreu.R
import com.dinube.bonpreu.TransactionAdapter
import kotlinx.android.synthetic.main.demo_dashboard_activity.*
import kotlinx.android.synthetic.main.demo_dashboard_activity.rv_transactions
import kotlinx.android.synthetic.main.layout_dashboard_grid_options.*
import kotlinx.android.synthetic.main.legal_terms_activity.*
import kotlinx.android.synthetic.main.legal_terms_activity.toolbar
import kotlinx.android.synthetic.main.salt_edge_dashboard_activity.*
import kotlinx.android.synthetic.main.sign_up_activity.*
import kotlin.random.Random

class DashboardActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_dashboard_activity)
        initializeToolbar()
        initializeImageViews()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        rv_transactions.layoutManager = LinearLayoutManager(this@DashboardActivity)
        rv_transactions.adapter = TransactionAdapter(getDemoData())
    }

    private fun getDemoData(): ArrayList<TransactionAdapter.TransactionItems> {
        val transactionItems: ArrayList<TransactionAdapter.TransactionItems> = ArrayList()

        for (i in 1..20){
            transactionItems.add(TransactionAdapter.TransactionItems(Random.nextInt(1,500).toString(), "",Random.nextInt(1,30).toString() + " Nov, 2019", "03AE56X"))
        }

        return transactionItems
    }

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