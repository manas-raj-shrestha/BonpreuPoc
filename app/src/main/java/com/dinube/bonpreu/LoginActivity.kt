package com.dinube.bonpreu

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.saltedge.SaltEdgeCredentialsPaymentActivity
import com.dinube.bonpreu.saltedge.SaltEdgeDirectPayActivity
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.api.common.*
import kotlinx.android.synthetic.main.activity_signup.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity: AppCompatActivity() {

    var for_payment = false
    var for_payment_creds = false
    var for_payment_direct = false


    companion object {
        private const val LOG_TAG = "SingularKeyFido2Demo"
        private const val REQUEST_CODE_REGISTER = 1
        private const val REQUEST_CODE_SIGN = 2
        private const val KEY_HANDLE_PREF = "key_handle"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        for_payment=intent.getBooleanExtra("for_payment",false)
        for_payment_creds=intent.getBooleanExtra("for_payment_creds",false)
        for_payment_direct=intent.getBooleanExtra("for_payment_direct",false)

        if (for_payment){
            val sharedPref = this.getPreferences(Context.MODE_PRIVATE) ?: return
            usernameButton.setText(sharedPref.getString("user_name",""))
            usernameButton.visibility = View.GONE
            signupFido2Button.visibility = View.GONE
            resultText.text = "Please Wait"
            fido2AuthInitiate()
//            var btn = findViewById<Button>(R.id.signupFido2Button)
//            btn.performClick()
        }

        try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.e(
                    "MY KEY HASH:",
                    Base64.encodeToString(md.digest(), Base64.DEFAULT)
                )
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }

        signupFido2Button.text = "LOGIN"
        signupFido2Button.setOnClickListener {fido2AuthInitiate() }
    }

    //**********************************************************************************************************//
    //******************************* Android FIDO2 API Response ***********************************************//
    //**********************************************************************************************************//
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(LOG_TAG, "onActivityResult - requestCode: $requestCode, resultCode: $resultCode")

        when (resultCode) {
            RESULT_OK -> {
                data?.let {
                    if (it.hasExtra(Fido.FIDO2_KEY_ERROR_EXTRA)) {

                        val errorExtra = data.getByteArrayExtra(Fido.FIDO2_KEY_ERROR_EXTRA)
                        val authenticatorErrorResponse = AuthenticatorErrorResponse.deserializeFromBytes(errorExtra)
                        val errorName = authenticatorErrorResponse.errorCode.name
                        val errorMessage = authenticatorErrorResponse.errorMessage

                        Log.e(LOG_TAG, "errorCode.name: $errorName")
                        Log.e(LOG_TAG, "errorMessage: $errorMessage")

                        resultText.text = "An Error Ocurred\n\nError Name:\n$errorName\n\nError Message:\n$errorMessage"

                    } else if (it.hasExtra(Fido.FIDO2_KEY_RESPONSE_EXTRA)) {
                        val fido2Response = data.getByteArrayExtra(Fido.FIDO2_KEY_RESPONSE_EXTRA)
                        when (requestCode) {
//                            REQUEST_CODE_REGISTER -> fido2RegisterComplete(fido2Response)
                            REQUEST_CODE_SIGN -> fido2AuthComplete(fido2Response)
                        }
                    }
                }
            }
            RESULT_CANCELED -> {
                val result = "Operation is cancelled"
                resultText.text = result
                Log.d(LOG_TAG, result)
            }
            else -> {
                val result = "Operation failed, with resultCode: $resultCode"
                resultText.text = result
                Log.e(LOG_TAG, result)
            }
        }
    }

    //**********************************************************************************************************//
    //******************************* FIDO2 Authentication Step 1 **********************************************//
    //******************************* Get challenge from the Server ********************************************//
    //**********************************************************************************************************//
    private fun fido2AuthInitiate() {

        val result = JSONObject()
        val mediaType = "application/json".toMediaTypeOrNull()
        result.put("name", usernameButton.text.toString())
        val requestBody = RequestBody.create(mediaType, result.toString())
        try {
            RPApiService.getApi().authInitiate(requestBody).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val obj = JSONObject(response.body()?.string())
                        val c = obj?.getString("challenge")
                        val challenge = Base64.decode(c!!, BASE64_FLAG)
                        val allowCredentials = obj?.getJSONArray("allowCredentials")

                        fido2AndroidAuth(allowCredentials, challenge)

                        Log.d("response", response.message())
                    } else {
                        Log.d("response", response.errorBody().toString())
                        resultText.text = "Authentication Failed" + "\n" + response.toString()
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

    //**********************************************************************************************************//
    //******************************* FIDO2 Authentication Step 2 **********************************************//
    //******************************* Invoke Android FIDO2 API  ************************************************//
    //**********************************************************************************************************//
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
                        Base64.decode(item.getString("id"), BASE64_FLAG),
                        /* transports */ null
                    )
                )
            }

            val options = PublicKeyCredentialRequestOptions.Builder()
                .setRpId(RPID)
                .setAllowList(list)
                .setChallenge(challenge)
                .build()

            val fido2ApiClient = Fido.getFido2ApiClient(applicationContext)
            val fido2PendingIntentTask = fido2ApiClient.getSignIntent(options)
            fido2PendingIntentTask.addOnSuccessListener { fido2PendingIntent ->
                if (fido2PendingIntent.hasPendingIntent()) {
                    try {
                        Log.d(LOG_TAG, "launching Fido2 Pending Intent")
                        fido2PendingIntent.launchPendingIntent(this@LoginActivity, REQUEST_CODE_SIGN)
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }
        } catch(e:Exception) {
            e.printStackTrace()
        }
    }

    //**********************************************************************************************************//
    //******************************* FIDO2 Authentication Step 3 **********************************************//
    //**************** Send Signed Challenge (Assertion) to the Server for verification ************************//
    //**********************************************************************************************************//
    private fun fido2AuthComplete(fido2Response: ByteArray) {

        val assertionResponse = AuthenticatorAssertionResponse.deserializeFromBytes(fido2Response)
        val credId = Base64.encodeToString(assertionResponse.keyHandle, BASE64_FLAG)
        val signature = Base64.encodeToString(assertionResponse.signature, BASE64_FLAG)
        val authenticatorData = Base64.encodeToString(assertionResponse.authenticatorData, BASE64_FLAG)
        val clientDataJson = Base64.encodeToString(assertionResponse.clientDataJSON, BASE64_FLAG)


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

        try {
            RPApiService.getApi().authComplete("name=${usernameButton.text.toString()}",requestBody).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        resultText.text = "Authentication Successful"
                        Log.d("response", response.message())

                        if (for_payment_creds){
                            Toast.makeText(this@LoginActivity,"Authentication Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity,
                                SaltEdgeCredentialsPaymentActivity::class.java))
                        }else if (for_payment_direct){
                            Toast.makeText(this@LoginActivity,"Authentication Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity,
                                SaltEdgeDirectPayActivity::class.java))
                        }else{
                            val sharedPref = this@LoginActivity.getPreferences(Context.MODE_PRIVATE)
                                ?: return
                            with (sharedPref.edit()) {
                                putString(("user_name"), usernameButton.text.toString())
                                commit()
                            }

                            Toast.makeText(this@LoginActivity,"Authentication Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                        }

                    } else {
                        Log.d("response", response.errorBody().toString())
                        resultText.text = "Authentication Failed" + "\n" + response.toString()
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