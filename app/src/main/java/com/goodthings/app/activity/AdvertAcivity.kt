package com.goodthings.app.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bumptech.glide.Glide
import com.goodthings.app.R
import kotlinx.android.synthetic.main.activity_advert_acivity.*

class AdvertAcivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_advert_acivity)
        val imgurl = intent.getStringExtra("imgurl")
        Glide.with(this@AdvertAcivity).load(imgurl).into(guide_img)
        coundDownTimer.start()
        guide_go.setOnClickListener {
            coundDownTimer.cancel()
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    val coundDownTimer = object: CountDownTimer(4000,1000){
        override fun onTick(p0: Long) {
            guide_go.text = "跳过 ${(p0/1000).toInt()}"
        }

        override fun onFinish() {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }
}
