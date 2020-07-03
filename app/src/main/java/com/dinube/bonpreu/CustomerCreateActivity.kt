package com.dinube.bonpreu

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.data.saltedgedata.customer.createcustomer.CustomerCreationRequest
import com.dinube.bonpreu.data.saltedgedata.customer.createcustomer.CustomerCreationResponse
import com.dinube.bonpreu.data.saltedgedata.customer.createcustomer.Data
import com.dinube.bonpreu.retroInterface.BudCalls
import com.dinube.bonpreu.saltedge.SaltEdgeDashboardActivity
import kotlinx.android.synthetic.main.create_customer_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerCreateActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_customer_activity)

        btn_create_customer.setOnClickListener {
            if (edt_customer_identifier.text.toString().isNotEmpty())
                this.createCustomer()
        }
    }

    private fun createCustomer() {
        val request = ServiceBuilder.buildService(BudCalls::class.java)


        val call = request.createCustomer("https://www.saltedge.com/api/v5/customers", CustomerCreationRequest(
            Data(edt_customer_identifier.text.toString())
        ))

        call.enqueue(object : Callback<CustomerCreationResponse> {
            override fun onResponse(call: Call<CustomerCreationResponse>, response: Response<CustomerCreationResponse>) {

                if (response.isSuccessful){
                    var intent = Intent(this@CustomerCreateActivity, SaltEdgeDashboardActivity::class.java)
                    intent.putExtra("customer_id", response.body()!!.data.id )
                    startActivity(intent)
                }else{
                    Toast.makeText(this@CustomerCreateActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<CustomerCreationResponse>, t: Throwable) {
                Toast.makeText(this@CustomerCreateActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}