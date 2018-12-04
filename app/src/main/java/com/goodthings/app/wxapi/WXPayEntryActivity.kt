package com.goodthings.app.wxapi

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.goodthings.app.R
import com.goodthings.app.bean.EventBean
import com.goodthings.app.application.Consts
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.greenrobot.eventbus.EventBus

class WXPayEntryActivity : AppCompatActivity(), IWXAPIEventHandler {

    lateinit var api: IWXAPI;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_result)
        api = WXAPIFactory.createWXAPI(this, Consts.WXAPPID)
        api.handleIntent(intent,this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        api.handleIntent(intent,this)
    }

    override fun onReq(p0: BaseReq?) {
    }

    override fun onResp(p0: BaseResp?) {
        if(p0?.type == ConstantsAPI.COMMAND_PAY_BY_WX){
            Log.i("wxPay",p0?.errCode.toString())
            EventBus.getDefault().post(EventBean<Int>(1,p0?.errCode))
            finish()
        }
    }
}
