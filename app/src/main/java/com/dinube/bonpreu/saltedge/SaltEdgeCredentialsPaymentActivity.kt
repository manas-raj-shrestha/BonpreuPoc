package com.dinube.bonpreu.saltedge

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.ServiceBuilder
import com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.PayWithCredsResponse
import com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.request.Credentials
import com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.request.Data
import com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.request.PayWithCredsRequest
import com.dinube.bonpreu.data.saltedgedata.payment.paywithcreds.request.PaymentAttributes
import com.dinube.bonpreu.retroInterface.BudCalls
import kotlinx.android.synthetic.main.salt_edge_pay_creds.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SaltEdgeCredentialsPaymentActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.salt_edge_pay_creds)

        setClickListener()
    }

    private fun setClickListener() {
        btn_pay.setOnClickListener {
            if(edt_username.text.isNotEmpty() && edt_password.text.isNotEmpty())
                initiatePayment()
            else
                Toast.makeText(this@SaltEdgeCredentialsPaymentActivity, "Please enter both username and password", Toast.LENGTH_LONG).show()
        }
    }

    private fun initiatePayment() {
            val request =
                ServiceBuilder.buildService(BudCalls::class.java)

            val credentials =  Credentials( edt_password.text.toString(),edt_username.text.toString())

            val data = Data("SEPA",false,credentials,
                PaymentAttributes(),"fake_client_xf", intent.getStringExtra("customer_id")
            )

            val call = request.payWihCreds("https://www.saltedge.com/api/payments/v1/payments", PayWithCredsRequest(data))

            call.enqueue(object : Callback<PayWithCredsResponse> {
                override fun onResponse(call: Call<PayWithCredsResponse>, response: Response<PayWithCredsResponse>) {
                    if (response.isSuccessful){

                        Toast.makeText(this@SaltEdgeCredentialsPaymentActivity, "Payment initiated successfully. Please check saltedge dashboard for more information", Toast.LENGTH_SHORT).show()

                    }else{

                        Toast.makeText(this@SaltEdgeCredentialsPaymentActivity, "Error Connecting Application", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<PayWithCredsResponse>, t: Throwable) {
                    Log.e("Error","${t.message}" )
                    Toast.makeText(this@SaltEdgeCredentialsPaymentActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
}