package com.dinube.bonpreu

import android.R.attr.packageNames
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.api.common.*
import kotlinx.android.synthetic.main.activity_signup.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateEncodingException
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


var RP_SERVER_URL = "https://singularkey-webauthn.herokuapp.com/";  //e.g., https://api.singularkey.com
var RPID = "singularkey-webauthn.herokuapp.com"                     // e.g., api.yourcompany.com
const val BASE64_FLAG = Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE


class SignupActivity: AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "SingularKeyFido2Demo"
        private const val REQUEST_CODE_REGISTER = 1
        private const val REQUEST_CODE_SIGN = 2
        private const val KEY_HANDLE_PREF = "key_handle"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

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

        try {
            val info: PackageInfo = applicationContext.getPackageManager()
                .getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            val cert = info.signatures[0].toByteArray()
            val input: InputStream = ByteArrayInputStream(cert)
            val cf: CertificateFactory = CertificateFactory.getInstance("X509")
            val c: X509Certificate = cf.generateCertificate(input) as X509Certificate
            val md = MessageDigest.getInstance("SHA1")
            Log.e("android:apk-key-hash:" ,
                    Base64.encodeToString(
                        md.digest(c.getEncoded()),
                        Base64.DEFAULT or Base64.NO_WRAP or Base64.NO_PADDING)
                    )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: CertificateEncodingException) {
            e.printStackTrace()
        }



        signupFido2Button.setOnClickListener {fido2RegisterInitiate() }
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
                            REQUEST_CODE_REGISTER -> fido2RegisterComplete(fido2Response)
//                            REQUEST_CODE_SIGN -> fido2AuthComplete(fido2Response)
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
    //******************************* FIDO2 Registration Step 1 ************************************************//
    //******************************* Get challenge from the Server ********************************************//
    //**********************************************************************************************************//
    private fun fido2RegisterInitiate() {

        val result = JSONObject()
        val mediaType = "application/json".toMediaTypeOrNull()

        result.put("name", usernameButton.text.toString())

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
                        val challenge = Base64.decode(c!!, BASE64_FLAG)
                        var rpname = intiateResponse?.getJSONObject("rp")!!.getString("name")
                        var username = intiateResponse?.getJSONObject("user")!!.getString("name")
                        var userId = intiateResponse?.getJSONObject("user")!!.getString("id")

                        var authenticatorAttachement = ""
                        if(intiateResponse.has("authenticatorSelection")){
                            if(intiateResponse?.getJSONObject("authenticatorSelection").has("authenticatorAttachment")) {
                                authenticatorAttachement = intiateResponse?.getJSONObject("authenticatorSelection")?.getString("authenticatorAttachment")!!
                                Log.d(LOG_TAG, "authenticatorAttachement $authenticatorAttachement")
                            }
                        }

                        val attestation = intiateResponse?.getString("attestation")
                        Log.d(LOG_TAG, attestation)
                        var attestationPreference: AttestationConveyancePreference = AttestationConveyancePreference.NONE
                        if(attestation == "direct") {
                            attestationPreference = AttestationConveyancePreference.DIRECT
                        } else if(attestation == "indirect") {
                            attestationPreference = AttestationConveyancePreference.INDIRECT
                        } else if(attestation == "none") {
                            attestationPreference = AttestationConveyancePreference.NONE
                        }

                        fido2AndroidRegister(rpname, challenge, userId, username,authenticatorAttachement,attestationPreference)
                    } else {
                        resultText.text = response.toString()
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d(LOG_TAG, t.message)
                    resultText.text = t.message
                }
            })
        } catch(e:Exception) {
            e.printStackTrace()
        }

    }
    //**********************************************************************************************************//
    //******************************* FIDO2 Registration Step 2 ************************************************//
    //******************************* Invoke Android FIDO2 API  ************************************************//
    //**********************************************************************************************************//

    private fun fido2AndroidRegister(
        rpname: String,
        challenge: ByteArray,
        userId: String,
        userName: String?,
        authenticatorAttachment:String?,
        attestationPreference:AttestationConveyancePreference
    ) {

        try {
            val options = PublicKeyCredentialCreationOptions.Builder()
                .setAttestationConveyancePreference(attestationPreference)
                .setRp(PublicKeyCredentialRpEntity(RPID, rpname, null))
                .setUser(
                    PublicKeyCredentialUserEntity(
                        userId.toByteArray(),
                        userId,
                        null,
                        userName
                    )
                )
                .setChallenge(challenge)
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

            val fido2ApiClient = Fido.getFido2ApiClient(applicationContext)
            val fido2PendingIntentTask = fido2ApiClient.getRegisterIntent(options.build())
            fido2PendingIntentTask.addOnSuccessListener { fido2PendingIntent ->
                if (fido2PendingIntent.hasPendingIntent()) {
                    try {
                        Log.d(LOG_TAG, "launching Fido2 Pending Intent")
                        fido2PendingIntent.launchPendingIntent(
                            this@SignupActivity,
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

    //**********************************************************************************************************//
    //******************************* FIDO2 Registration Step 3 ************************************************//
    //***************************** Send Signed Challenge (Attestation) to the Server for validation ***********//
    //**********************************************************************************************************//
    private fun fido2RegisterComplete(fido2Response: ByteArray) {
        val attestationResponse = AuthenticatorAttestationResponse.deserializeFromBytes(fido2Response)
        val credId = Base64.encodeToString(attestationResponse.keyHandle, BASE64_FLAG)
        val clientDataJson = Base64.encodeToString(attestationResponse.clientDataJSON, BASE64_FLAG)
        val attestationObjectBase64 = Base64.encodeToString(attestationResponse.attestationObject, Base64.DEFAULT)


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
            RPApiService.getApi().registerComplete("name=${usernameButton.text.toString()}",requestBody).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        resultText.text = "Registration Successful"
                        Log.d("response", response.message())

                        Toast.makeText(this@SignupActivity,"Registration Successful",Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this@SignupActivity,RegistrationActivity::class.java))

                    } else {
                        resultText.text = "Registration Failed" + "\n" + response.toString()
                        Log.d("response", response.errorBody().toString())
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