package com.dinube.bonpreu.demo.signup

import android.content.Context
import android.content.Context.*
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.dinube.bonpreu.BaseActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.demo.bankconnection.providers.ProviderSelectionActivity
import com.dinube.bonpreu.demo.dashboard.DashboardActivity
import com.dinube.bonpreu.demo.signup.contracts.SignUpPresenterView

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.android.synthetic.main.bottom_sheet_signup.*
import kotlinx.android.synthetic.main.legal_terms_activity.toolbar
import kotlinx.android.synthetic.main.sign_up_activity.*

class SignUpActivity: BaseActivity(), SignUpPresenterView{

   lateinit var standardBottomSheetBehavior : BottomSheetBehavior<ConstraintLayout>
    var presenter: SignUpPresenter = SignUpPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_activity)

        initializeToolbar()
        initializeImageViews()
        initializeClickListeners()

        standardBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_sign_up)

        standardBottomSheetBehavior.state = STATE_HIDDEN

        btn_auto_signup.setOnClickListener {
            presenter.fido2RegisterInitiate(edt_phone_number.text.toString()) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("LOG_TAG", "onActivityResult - requestCode: $requestCode, resultCode: $resultCode")

        presenter.onResult(requestCode, resultCode, data)
    }

    private fun initializeClickListeners() {
        btn_login.setOnClickListener {
            Handler().postDelayed({standardBottomSheetBehavior.state = STATE_EXPANDED},500)
            hideKeyboard(this@SignUpActivity)

        }
    }

    private fun initializeImageViews() {
        var stream = assets?.open("fido_icon.png");
        iv_fido_icon.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()

        stream = assets?.open("singular_key_icon.png");
        iv_singular_key_icon.setImageBitmap(BitmapFactory.decodeStream(stream))
        stream?.close()
    }

    private fun initializeToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Registra't"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onSingularKeyError(error: String) {
        Log.e("here","here")
    }

    override fun onRegistrationSuccessful() {
        val sharedPref = getSharedPreferences(
            "dinube_pref", Context.MODE_PRIVATE)

        with (sharedPref.edit()) {
            putString("username", edt_phone_number.text.toString())
            commit()
        }

        startActivity(Intent(this, ProviderSelectionActivity::class.java))
    }
}