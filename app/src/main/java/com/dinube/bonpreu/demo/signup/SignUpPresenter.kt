package com.dinube.bonpreu.demo.signup

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.RPApiService
import com.dinube.bonpreu.demo.signup.contracts.SignUpPresenterView
import com.dinube.bonpreu.demo.signup.contracts.SignUpViewPresenter
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.api.common.*
import kotlinx.android.synthetic.main.sign_up_activity.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpPresenter(context: Context): SignUpViewPresenter{

    private lateinit var signUpPresenterView: SignUpPresenterView
    private lateinit var username: String
    private  var context: Context = context

    companion object {
        const val BASE64_FLAG = Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE
        const val LOG_TAG: String = "SignUpPresenter"
        private const val REQUEST_CODE_REGISTER = 1
        private const val REQUEST_CODE_SIGN = 2
        private const val KEY_HANDLE_PREF = "key_handle"
    }

    init {
        if (context is SignUpPresenterView)
            signUpPresenterView = context
    }

    override fun fido2RegisterInitiate(username: String) {

        this.username = username

        val result = JSONObject()
        val mediaType = "application/json".toMediaTypeOrNull()

        result.put("name", username)

        //Optional
        val jsonObject = JSONObject()
        //jsonObject.put("authenticatorAttachment","platform")
        jsonObject.put("userVerification","required")
        result.put("authenticatorSelection",jsonObject)

        val requestBody = RequestBody.create(mediaType, result.toString())

        try {
            RPApiService.getApi().registerInitiate(requestBody).enqueue(object :
                Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        var obj = JSONObject(response.body()?.string())
                        var intiateResponse = obj.getJSONObject("initiateRegistrationResponse")
                        val c = intiateResponse?.getString("challenge")
                        val challenge = Base64.decode(c!!, Companion.BASE64_FLAG)
                        var rpname = intiateResponse?.getJSONObject("rp")!!.getString("name")
                        var username = intiateResponse?.getJSONObject("user")!!.getString("name")
                        var userId = intiateResponse?.getJSONObject("user")!!.getString("id")

                        var authenticatorAttachment = ""
                        if(intiateResponse.has("authenticatorSelection")){
                            if(intiateResponse?.getJSONObject("authenticatorSelection").has("authenticatorAttachment")) {
                                authenticatorAttachment = intiateResponse?.getJSONObject("authenticatorSelection")?.getString("authenticatorAttachment")!!
                                Log.e(LOG_TAG, "authenticatorAttachement $authenticatorAttachment")
                            }
                        }

                        val attestation = intiateResponse?.getString("attestation")
                        Log.e(LOG_TAG, attestation)
                        var attestationPreference: AttestationConveyancePreference = AttestationConveyancePreference.NONE
                        if(attestation == "direct") {
                            attestationPreference = AttestationConveyancePreference.DIRECT
                        } else if(attestation == "indirect") {
                            attestationPreference = AttestationConveyancePreference.INDIRECT
                        } else if(attestation == "none") {
                            attestationPreference = AttestationConveyancePreference.NONE
                        }

                        fido2AndroidRegister(rpname, challenge, userId, username,authenticatorAttachment,attestationPreference)
                    } else {
                        signUpPresenterView.onSingularKeyError("Error")
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    signUpPresenterView.onSingularKeyError("Error")
                }
            })
        } catch(e:Exception) {
            e.printStackTrace()
        }

    }

    override fun onResult(requestCode: Int,resultCode: Int, data: Intent?) {
        when (resultCode) {
            AppCompatActivity.RESULT_OK -> {
                data?.let {
                    if (it.hasExtra(Fido.FIDO2_KEY_ERROR_EXTRA)) {

                        val errorExtra = data.getByteArrayExtra(Fido.FIDO2_KEY_ERROR_EXTRA)
                        val authenticatorErrorResponse =
                            AuthenticatorErrorResponse.deserializeFromBytes(errorExtra)
                        val errorName = authenticatorErrorResponse.errorCode.name
                        val errorMessage = authenticatorErrorResponse.errorMessage

                    } else if (it.hasExtra(Fido.FIDO2_KEY_RESPONSE_EXTRA)) {
                        val fido2Response = data.getByteArrayExtra(Fido.FIDO2_KEY_RESPONSE_EXTRA)
                        when (requestCode) {
                            REQUEST_CODE_REGISTER -> fido2RegisterComplete(fido2Response)
                        }
                    }
                }
            }
            AppCompatActivity.RESULT_CANCELED -> {
                val result = "Operation is cancelled"
//                resultText.text = result
                Log.e(LOG_TAG, result)
            }
            else -> {
                val result = "Operation failed, with resultCode: $resultCode"
//                resultText.text = result
                Log.e(LOG_TAG, result)
            }
        }
    }

    private fun fido2AndroidRegister(
        rpname: String,
        challenge: ByteArray?,
        userId: String,
        username: String,
        authenticatorAttachment: String,
        attestationPreference: AttestationConveyancePreference
    ) {
        try {
            val options = PublicKeyCredentialCreationOptions.Builder()
                .setAttestationConveyancePreference(attestationPreference)
                .setRp(PublicKeyCredentialRpEntity("singularkey-webauthn.herokuapp.com", rpname, null))
                .setUser(
                    PublicKeyCredentialUserEntity(
                        userId.toByteArray(),
                        userId,
                        null,
                        username
                    )
                )
                .setChallenge(challenge!!)
                .setParameters(
                    listOf(
                        PublicKeyCredentialParameters(
                            PublicKeyCredentialType.PUBLIC_KEY.toString(),
                            EC2Algorithm.ES256.algoValue
                        )
                    )
                )

            if (authenticatorAttachment != "") {
                val builder = AuthenticatorSelectionCriteria.Builder()
                builder.setAttachment(Attachment.fromString("platform"))
                options.setAuthenticatorSelection(builder.build())
            }

            val fido2ApiClient = Fido.getFido2ApiClient(context)
            val fido2PendingIntentTask = fido2ApiClient.getRegisterIntent(options.build())
            fido2PendingIntentTask.addOnSuccessListener { fido2PendingIntent ->
                if (fido2PendingIntent.hasPendingIntent()) {
                    try {
                        Log.e(LOG_TAG, "launching Fido2 Pending Intent")
                        fido2PendingIntent.launchPendingIntent(
                            context as SignUpActivity,
                            REQUEST_CODE_REGISTER
                        )
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fido2RegisterComplete(fido2Response: ByteArray) {
        val attestationResponse = AuthenticatorAttestationResponse.deserializeFromBytes(fido2Response)
        val credId = Base64.encodeToString(attestationResponse.keyHandle, BASE64_FLAG)
        val clientDataJson = Base64.encodeToString(attestationResponse.clientDataJSON, BASE64_FLAG)
        val attestationObjectBase64 = Base64.encodeToString(attestationResponse.attestationObject, Base64.DEFAULT)

        Log.e("here","here")
        val webAuthnResponse = JSONObject()
        val response = JSONObject()

        response.put("attestationObject", attestationObjectBase64)
        response.put("clientDataJSON", clientDataJson)


        webAuthnResponse.put("type", "public-key")
        webAuthnResponse.put("id", credId)
        webAuthnResponse.put("rawId", credId)
        webAuthnResponse.put("getClientExtensionResults", JSONObject())
        webAuthnResponse.put("response", response)

        val mediaType = "application/json".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, webAuthnResponse.toString())

        try {
            RPApiService.getApi().registerComplete("name=${username}",requestBody).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
//                        resultText.text = "Registration Successful"
                        Log.e("response", response.message())

                        Toast.makeText(context,"Registration Successful", Toast.LENGTH_SHORT).show()
                        signUpPresenterView.onRegistrationSuccessful()
//                        startActivity(Intent(this@SignUpActivity,RegistrationActivity::class.java))

                    } else {
//                        resultText.text = "Registration Failed" + "\n" + response.toString()
                        Log.e("response", response.errorBody().toString())
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("response", t.message)

                }
            })
        } catch(e:Exception) {
            e.printStackTrace()
        }
    }


}