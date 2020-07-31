package com.dinube.bonpreu.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.LoginActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.demo.onboarding.OnBoardingActivity
import java.util.*
import kotlin.concurrent.schedule


class SplashActivity : AppCompatActivity() {

    var username: String? = "NA"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPref = getSharedPreferences(
            "dinube_pref", Context.MODE_PRIVATE)
        username =
            sharedPref.getString("username", "NA")


    }

    override fun onResume() {
        super.onResume()
        Timer().schedule(2000) {
            this@SplashActivity.runOnUiThread {
                if (username != "NA"){
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }else{
                    val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }

        }
    }
}
