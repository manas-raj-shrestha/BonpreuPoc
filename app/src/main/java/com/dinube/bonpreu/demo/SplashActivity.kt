package com.dinube.bonpreu.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dinube.bonpreu.R
import com.dinube.bonpreu.demo.dashboard.DashboardActivity
import com.dinube.bonpreu.demo.login.LoginActivity
import com.dinube.bonpreu.demo.onboarding.OnBoardingActivity
import java.util.*
import kotlin.concurrent.schedule


class SplashActivity : AppCompatActivity() {

    var username: String? = "NA"
    private var time =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sharedPref = getSharedPreferences(
            "dinube_pref", Context.MODE_PRIVATE)
        username =
            sharedPref.getString("username", "NA")

//        time = if (username != "NA"){
//            0
//        } else{
//            2000
//        }


    }

    override fun onResume() {
        super.onResume()
        if (username != "NA"){
            val intent = Intent(this@SplashActivity, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }else{
        Timer().schedule(2000) {
            this@SplashActivity.runOnUiThread {
                    val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
            }

        }
    }
}
