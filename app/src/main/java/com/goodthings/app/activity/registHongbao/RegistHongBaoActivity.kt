package com.goodthings.app.activity.registHongbao

import android.os.Bundle
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import kotlinx.android.synthetic.main.activity_regist_hong_bao.*
import org.jetbrains.anko.imageResource

class RegistHongBaoActivity :
        BaseActivity<RegistHongBaoContract.RegistHongBaoView,RegistHongBaoContract.RegistHongBaoPresenter>(),
        RegistHongBaoContract.RegistHongBaoView{

    override var presenter: RegistHongBaoContract.RegistHongBaoPresenter = RegistHongBaoPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_hong_bao)
        hongbao_topback.setOnClickListener {
            finish()
        }

        var hongbao=intent.getDoubleExtra("hongbao",0.00)
        regist_hb_money.text = "${hongbao}元"
        registhb_share.setOnClickListener {
            openShareWindow(this@RegistHongBaoActivity,
                    hongbao_root, Consts.URL+"sysUser/unauth/countRegist",
                    "给你发红包！点击和我一起领现金，数量有限>>",
                    "发红包啦！最高100元，真的现金哦，可提现。",
                    "665688451905141771.png")
        }
    }

    override fun shareResult(isSuccess: Boolean, msg: String) {
        super.shareResult(isSuccess, msg)
        if(isSuccess){
            presenter.registShareGoldEnvelopes()
        }
    }

    override fun shareGoldSuccess() {
        registhb_share.imageResource = R.mipmap.share_disable
        registhb_share.isEnabled = false
    }
}
