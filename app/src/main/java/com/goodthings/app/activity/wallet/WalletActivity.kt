package com.goodthings.app.activity.wallet

import android.content.Intent
import android.os.Bundle
import com.goodthings.app.R
import com.goodthings.app.activity.web.WebActivity
import com.goodthings.app.activity.gold.GoldActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.WalletBean
import kotlinx.android.synthetic.main.activity_wallet.*
import kotlinx.android.synthetic.main.top_bar.*

class WalletActivity :
        BaseActivity<WalletContract.WalletView,WalletContract.WalletPresenter>(),
        WalletContract.WalletView {

    override var presenter: WalletContract.WalletPresenter = WalletPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wallet)
        topback.setOnClickListener { onBackPressed() }
        toptitle.text = "我的钱包"
        wallet_gold_layout.setOnClickListener {
            startActivity(Intent(this@WalletActivity,GoldActivity::class.java))
        }
        wallet_cash_layout.setOnClickListener {
            var intent = Intent(this@WalletActivity, WebActivity::class.java)
            intent.putExtra("url","${Consts.URL}/user/myTranRecord")
            startActivity(intent)
        }

        presenter.start()
    }

    override fun updateData(it: WalletBean?) {
        wallet_acount_gold.text = "${it?.coinChangeBalance}"
        wallet_acount_cash.text = "${it?.balance}"
        wallet_acount_jb.text = String.format(resources.getString(R.string.my_gold),"${it?.coinBalance}")
        wallet_acount_rate.text = "昨日汇率${it?.balanceL}%"
        wallet_acount_js.text = "昨日最后金币（转）零钱结算：${it?.balanceH}金币= ${it?.balanceC} 元"
    }
}
