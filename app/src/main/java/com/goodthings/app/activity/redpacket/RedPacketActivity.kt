package com.goodthings.app.activity.redpacket

import android.os.Bundle
import android.text.Html
import android.view.View
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_red_packet.*
import kotlinx.android.synthetic.main.top_bar.*

class RedPacketActivity :
        BaseActivity<RedPacketContract.RedPacketView,RedPacketContract.RedPacketPresenter>(),
        RedPacketContract.RedPacketView{

    override var presenter: RedPacketContract.RedPacketPresenter = RedPacketPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_red_packet)
        topback.setOnClickListener {
            finish()
        }
        toptitle.text = "天天拆红包 领百元现金"

        redpacket_bt.setOnClickListener {
            openShareWindow(this@RedPacketActivity,
                    redpacket_root,Consts.URL+"sysUser/unauth/countRegist",
                    "给你发红包！点击和我一起领现金，数量有限>>",
                    "发红包啦！最高100元，真的现金哦，可提现。",
                    "665688451905141771.png")

        }

        if(intent.hasExtra("hongbao")){
            var jb=intent.getStringExtra("hongbao")
            if(jb!=null&&!jb.isEmpty()){
                showHongBao(true,jb)
            }

        }
    }

    override fun shareResult(isSuccess: Boolean, msg: String) {
        if(isSuccess){
            presenter.dailyRedEnvelopes()
        }
    }

    override fun showHongBao(isSuccess: Boolean, msg: String) {
        if(isSuccess) {
            redpacket_bt.visibility = View.GONE
            redpacket_bg.setBackgroundResource(R.mipmap.redpacket_after_bg)
            redpacket_result.visibility = View.VISIBLE
            redpacket_result.text = Html.fromHtml("今日已领取 <html><font color='#FFFCE307'>${msg}元</font></html> \n明日请再来")
        }else{
            showMessage(msg)
        }
    }

}
