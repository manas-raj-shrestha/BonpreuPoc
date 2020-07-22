package com.dinube.bonpreu.demo.bankconnection.providers

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.dinube.bonpreu.BaseActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.ServiceBuilder
import com.dinube.bonpreu.data.afterbanks.Provider
import com.dinube.bonpreu.demo.bankconnection.consent.BankConsentActivity
import com.dinube.bonpreu.retroInterface.BudCalls
import kotlinx.android.synthetic.main.legal_terms_activity.toolbar
import kotlinx.android.synthetic.main.provider_selection_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Fetches all available bank or providers and lists them for user selection
 */
class ProviderSelectionActivity : BaseActivity(), ProviderSelectionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.provider_selection_activity)
        initializeToolbar()

        rv_providers.visibility = View.GONE

        fetchProviders()
    }

    private fun fetchProviders() {

        val request = ServiceBuilder.buildService(BudCalls::class.java)

        val call = request.fetchAfterbankProviders("https://apipsd2.afterbanks.com/listOfSupportedBanks", "es")
        call.enqueue(object : Callback<ArrayList<Provider>> {
            override fun onResponse(call: Call<ArrayList<Provider>>, response: Response<ArrayList<Provider>>) {

                if (response.isSuccessful){
                    progress_bar.visibility = View.GONE
                    initializeRv(response.body())
                }else{
                    Toast.makeText(this@ProviderSelectionActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<ArrayList<Provider>>, t: Throwable) {
                Toast.makeText(this@ProviderSelectionActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initializeRv(body: ArrayList<Provider>?) {
        rv_providers.visibility = View.VISIBLE
        rv_providers.layoutManager = LinearLayoutManager(this@ProviderSelectionActivity)
        rv_providers.adapter = ProviderRvAdapter(body)
    }

    private fun initializeToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Select a bank"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onProviderSelected(position: Int) {
        startActivity(Intent(this@ProviderSelectionActivity, BankConsentActivity::class.java))
    }


}