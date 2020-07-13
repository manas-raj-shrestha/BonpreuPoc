package com.dinube.bonpreu.demo.onboarding

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dinube.bonpreu.R
import kotlinx.android.synthetic.main.tutorial_fragment.view.*

class TutorialFragment(position: Int): Fragment() {

    private val imageAssets = arrayOf("image_tutorial_1.png","image_tutorial_2.png","image_tutorial_3.png")
    val tickerTexts = arrayOf("Tutorial 1", "Tutorial 2", "Tutorial 3")
    private val position: Int = position

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(activity).inflate(
            R.layout.tutorial_fragment, container, false)
        var stream = activity?.assets?.open(imageAssets[position]);
        var bitmap = BitmapFactory.decodeStream(stream)

        view.iv_tutorial_image.setImageBitmap(bitmap)

        return view
    }
}