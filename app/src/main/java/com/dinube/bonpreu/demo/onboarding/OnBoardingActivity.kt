package com.dinube.bonpreu.demo.onboarding

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dinube.bonpreu.R
import com.dinube.bonpreu.demo.login.LoginActivity
import com.dinube.bonpreu.demo.signup.LegalTermsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.demo_activity_onboarding.*
import kotlinx.android.synthetic.main.demo_activity_onboarding.iv_logo as iv_logo1

class OnBoardingActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_onboarding)

        initiateViewPager()
        setContinueButtonAction()
        setLoginButtonAction()

    }

    private fun setLoginButtonAction() {
        tv_already_signed_in.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
    }

    private fun setContinueButtonAction() {
        btn_login.setOnClickListener {
            if (vp_tutorial.currentItem == 2){
                startActivity(Intent(this, LegalTermsActivity::class.java))
            }else{
                vp_tutorial.currentItem += 1
            }
        }
    }

    private fun initiateViewPager() {
        vp_tutorial.adapter =
            TutorialPagerAdapter(
                supportFragmentManager
            )
        page_view_indicator.count = 3
    }
}

class TutorialPagerAdapter(supportFragmentManager: FragmentManager) : FragmentStatePagerAdapter(supportFragmentManager) {
    override fun getItem(position: Int): Fragment {
        return TutorialFragment(position)
    }

    override fun getCount(): Int {
        return 3
    }
}
