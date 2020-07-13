package com.dinube.bonpreu.demo.signup

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.dinube.bonpreu.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlinx.android.synthetic.main.bottom_sheet_signup.*
import kotlinx.android.synthetic.main.legal_terms_activity.toolbar
import kotlinx.android.synthetic.main.sign_up_activity.*

class SignUpActivity: AppCompatActivity() {

   lateinit var standardBottomSheetBehavior : BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_activity)

        initializeToolbar()
        initializeImageViews()
        initializeClickListeners()

        standardBottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_sign_up)

        standardBottomSheetBehavior.state = STATE_HIDDEN
    }

    private fun initializeClickListeners() {
        btn_continue.setOnClickListener {
            standardBottomSheetBehavior.state = STATE_EXPANDED
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
}