package com.dinube.bonpreu.demo.payment

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.dinube.bonpreu.BaseActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.RPApiService
import com.dinube.bonpreu.demo.dashboard.DashboardActivity
import com.dinube.bonpreu.demo.login.LoginActivity
import com.dinube.bonpreu.demo.signup.SignUpPresenter
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.api.common.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.bottom_sheet_amount_pay.*
import kotlinx.android.synthetic.main.bottom_sheet_signup.*
import kotlinx.android.synthetic.main.legal_terms_activity.toolbar
import kotlinx.android.synthetic.main.payment_initiation_activity.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentUskActivity: BaseActivity() {
    lateinit var standardBottomSheetBehavior : BottomSheetBehavior<ConstraintLayout>

    companion object{
    private const val REQUEST_CODE_SIGN = 2}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.payment_initiation_activity)
        initializeToolbar()
        initializeImageViews()

        standardBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_amount_pay)

        standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        iv_authorize.setOnClickListener {
            fido2AuthInitiate()
//            startActivity(Intent(this,PaymentInitiateActivity::class.java))
        }
        tv_auth.setOnClickListener {
            fido2AuthInitiate()
//            startActivity(Intent(this,PaymentInitiateActivity::class.java))
        }

        cl_parent.setOnClickListener {
//            fido2AuthInitiate()
            Handler().postDelayed({standardBottomSheetBehavior.state =
                BottomSheetBehavior.STATE_EXPANDED
            },500)
        }
    }


    private fun initializeToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Realitzar pagament"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }


    private fun initializeImageViews() {
        var stream = assets?.open("dinube_logo.png");
        iv_dinube_logo.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()

         stream = assets?.open("tap_pay.png");
        iv_tap_pay.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()

        stream = assets?.open("barcode_sample.png");
        iv_barcode.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()
    }

    private fun fido2AndroidAuth(
        allowCredentials: JSONArray,
        challenge: ByteArray
    ) {
        try {
            val list = mutableListOf<PublicKeyCredentialDescriptor>()
            for (i in 0..(allowCredentials.length() - 1)) {
                val item = allowCredentials.getJSONObject(i)
                list.add(
                    PublicKeyCredentialDescriptor(
                        PublicKeyCredentialType.PUBLIC_KEY.toString(),
                        Base64.decode(item.getString("id"), SignUpPresenter.BASE64_FLAG),
                        /* transports */ null
                    )
                )
            }

            val options = PublicKeyCredentialRequestOptions.Builder()
                .setRpId("singularkey-webauthn.herokuapp.com" )
                .setAllowList(list)
                .setChallenge(challenge)
                .build()

            val fido2ApiClient = Fido.getFido2ApiClient(applicationContext)
            val fido2PendingIntentTask = fido2ApiClient.getSignIntent(options)
            fido2PendingIntentTask.addOnSuccessListener { fido2PendingIntent ->
                if (fido2PendingIntent.hasPendingIntent()) {
                    try {
                        Log.d("USK", "launching Fido2 Pending Intent")
                        fido2PendingIntent.launchPendingIntent(this@PaymentUskActivity,
                            REQUEST_CODE_SIGN
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }
        } catch(e:Exception) {
            e.printStackTrace()
        }
    }



    private fun fido2AuthInitiate() {
        val username =
            getSharedPreferences(
                "dinube_pref", Context.MODE_PRIVATE).getString("username", "NA")

        val result = JSONObject()
        val mediaType = "application/json".toMediaTypeOrNull()
        result.put("name", username)
        val requestBody = RequestBody.create(mediaType, result.toString())
        try {
            RPApiService.getApi().authInitiate(requestBody).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val obj = JSONObject(response.body()?.string())
                        val c = obj?.getString("challenge")
                        val challenge = Base64.decode(c!!, SignUpPresenter.BASE64_FLAG)
                        val allowCredentials = obj?.getJSONArray("allowCredentials")

                        fido2AndroidAuth(allowCredentials, challenge)

                        Log.d("response", response.message())
                    } else {
                        Log.d("response", response.errorBody().toString())
                        Toast.makeText(this@PaymentUskActivity, "Login Failed", Toast.LENGTH_SHORT).show()
//                        resultText.text = "Authentication Failed" + "\n" + response.toString()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("response", t.message)

                }
            })
        } catch(e:Exception) {
            e.printStackTrace()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("LOG_TAG", "onActivityResult - requestCode: $requestCode, resultCode: $resultCode")

        when (resultCode) {
            RESULT_OK -> {
                data?.let {
                    if (it.hasExtra(Fido.FIDO2_KEY_ERROR_EXTRA)) {

                        val errorExtra = data.getByteArrayExtra(Fido.FIDO2_KEY_ERROR_EXTRA)
                        val authenticatorErrorResponse = AuthenticatorErrorResponse.deserializeFromBytes(errorExtra)
                        val errorName = authenticatorErrorResponse.errorCode.name
                        val errorMessage = authenticatorErrorResponse.errorMessage

                    } else if (it.hasExtra(Fido.FIDO2_KEY_RESPONSE_EXTRA)) {
                        val fido2Response = data.getByteArrayExtra(Fido.FIDO2_KEY_RESPONSE_EXTRA)
                        when (requestCode) {
                            REQUEST_CODE_SIGN -> fido2AuthComplete(fido2Response)
                        }
                    }
                }
            }
            RESULT_CANCELED -> {
                val result = "Operation is cancelled"
//                resultText.text = result
                Log.d("LOG_TAG", result)
            }
            else -> {
                val result = "Operation failed, with resultCode: $resultCode"
//                resultText.text = result
                Log.e("LOG_TAG", result)
            }
        }
    }

    private fun fido2AuthComplete(fido2Response: ByteArray) {

        val assertionResponse = AuthenticatorAssertionResponse.deserializeFromBytes(fido2Response)
        val credId = Base64.encodeToString(assertionResponse.keyHandle, SignUpPresenter.BASE64_FLAG)
        val signature = Base64.encodeToString(assertionResponse.signature,
            SignUpPresenter.BASE64_FLAG
        )
        val authenticatorData = Base64.encodeToString(assertionResponse.authenticatorData,
            SignUpPresenter.BASE64_FLAG
        )
        val clientDataJson = Base64.encodeToString(assertionResponse.clientDataJSON,
            SignUpPresenter.BASE64_FLAG
        )


        val response = JSONObject()
        response.put("clientDataJSON", clientDataJson)
        response.put("signature", signature)
        response.put("userHandle", "")
        response.put("authenticatorData", authenticatorData)

        val jsonObject = JSONObject()
        jsonObject.put("type", "public-key")
        jsonObject.put("id", credId)
        jsonObject.put("rawId", credId)
        jsonObject.put("getClientExtensionResults", JSONObject())
        jsonObject.put("response", response)

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, jsonObject.toString())

        val username =
            getSharedPreferences(
                "dinube_pref", Context.MODE_PRIVATE).getString("username", "NA")

        try {
            RPApiService.getApi().authComplete("name=${username}",requestBody).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
//                        resultText.text = "Authentication Successful"
                        Log.d("response", response.message())


                        Toast.makeText(this@PaymentUskActivity,"Authentication Successful", Toast.LENGTH_SHORT).show()

                         startActivity(Intent(this@PaymentUskActivity, PaymentInitiateActivity::class.java))

                    } else {
                        Log.d("response", response.errorBody().toString())
//                        resultText.text = "Authentication Failed" + "\n" + response.toString()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("response", t.message)

                }
            })
        } catch(e:Exception) {
            e.printStackTrace()
        }
    }
}