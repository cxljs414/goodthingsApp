package com.goodthings.app.adapter

import android.app.Activity
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import java.util.ArrayList

class MyVpAdapter(private val context: Activity, articles: IntArray?) : PagerAdapter() {
    private val images = ArrayList<ImageView>()

    init {
        for (i in articles!!.indices) {
            val image = ImageView(context)
            image.scaleType = ImageView.ScaleType.CENTER_CROP
            image.setBackgroundResource(articles[i])
            images.add(image)
        }
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(images[position])
        return images[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(images[position])
    }
}