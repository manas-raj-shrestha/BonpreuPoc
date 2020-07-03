package com.dinube.bonpreu

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.palette.graphics.Palette
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var stream = assets.open("splash.png");
        var bitmap = BitmapFactory.decodeStream(stream)

        var streamEsclat = assets.open("esclat.png");
        var bitmapEsclat = BitmapFactory.decodeStream(streamEsclat)

        iv_logo.setImageBitmap(bitmap)

        btn_get_started.setOnClickListener { navigateToDashboard() }
        btn_get_started_salt_edge.setOnClickListener { navigateToSaltEdgeDashboard() }

        Handler().postDelayed({}, 2000)

    }

    private fun generateColor(bitmap: Bitmap, secondBitmap: Bitmap) {
        var dominantColorFist = 0
        var dominantColorSecond = 0

        Palette.from(bitmap).generate {
            var dominantColor = it?.getDarkMutedColor(Color.BLUE)
            dominantColorFist = dominantColor!!


            if (dominantColorFist!= 0 && dominantColorSecond !=0){
                animateBackground(dominantColorFist, dominantColorSecond)
            }
        }

        Palette.from(secondBitmap).generate {
            var dominantColor = it?.getDominantColor(Color.BLUE)
            dominantColorSecond = dominantColor!!


            if (dominantColorFist!= 0 && dominantColorSecond !=0){
                animateBackground(dominantColorFist, dominantColorSecond)
            }
        }


    }

    private fun animateBackground (dominantColor: Int, dominantColorSecond: Int){

        var valueAnimator = ValueAnimator.ofObject(ArgbEvaluator(),dominantColor, Color.parseColor("#1C2051"))
        valueAnimator.duration = 5000
        valueAnimator.addUpdateListener { constrain_layout.setBackgroundColor(it.animatedValue as Int) }
        valueAnimator.start()

        animateLogo()

    }

    private fun animateLogo () {

        var valueAnimator = ValueAnimator.ofFloat(1f, 0f)
        valueAnimator.duration = 2500
        valueAnimator.addUpdateListener {
            iv_logo.alpha = it.animatedValue as Float
        }

        valueAnimator.start()

        valueAnimator.doOnEnd {

            var streamEsclat = assets.open("esclat.png");
            var bitmapEsclat = BitmapFactory.decodeStream(streamEsclat)

            iv_logo.setImageBitmap(bitmapEsclat)

            var esclat = ValueAnimator.ofFloat(0f, 1f)
            esclat.duration = 2500
            esclat.addUpdateListener {
                iv_logo.alpha = it.animatedValue as Float
            }

            esclat.doOnEnd { this.navigateToDashboard() }

            esclat.start()
        }
    }

    private fun navigateToDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
    }

    private fun navigateToSaltEdgeDashboard() {
        startActivity(Intent(this, CustomerCreateActivity::class.java))
    }


}
