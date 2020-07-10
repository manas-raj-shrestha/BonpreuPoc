package com.dinube.bonpreu.afterbanks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dinube.bonpreu.R
import kotlinx.android.synthetic.main.demo_activity_onboarding.*

class DemoMainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_activity_onboarding)

        initiateViewPager()
        setContinueButtonAction()
    }

    private fun setContinueButtonAction() {
        btn_continue.setOnClickListener {
            if (vp_tutorial.currentItem == 2){

            }else{
                vp_tutorial.currentItem += 1
            }
        }
    }

    private fun initiateViewPager() {
        vp_tutorial.adapter = TutorialPagerAdapter(supportFragmentManager)
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
