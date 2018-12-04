package com.goodthings.app.activity.web

import android.os.Handler
import android.os.Message
import android.util.Log
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.User
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.SPUtil
import com.goodthings.app.util.StringUtil
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/10
 * 修改内容：
 * 最后修改时间：
 */
class WebPresenterImpl :
        BasePresenterImpl<WebContract.WebView>(),
        WebContract.WebPresenter{

    override fun retryLogin() {
        if(Consts.isLogined){
            quickCallJs("toSynLogin",null,"${Consts.user?.id}")
        }

    }

    lateinit var api: IWXAPI
    override fun afterAttachView() {
        super.afterAttachView()
        api = WXAPIFactory.createWXAPI(mView?.getContext(),null)
        api.registerApp(Consts.WXAPPID)
    }
    override fun callWX(content: String) {
        Thread(Runnable {
            kotlin.run {
                /*val url = "http://wxpay.wxutil.com/pub_v2/app/app_pay.php"
                val buf = WXUtil.httpGet(url)*/
                val msg = Message()
                msg.what = 1
                Log.i("Pay",content)
                try {
                    val json = JSONObject(content)
                    if(null != json && !json.has("retcode")){
                        val req = PayReq()
                        req.appId = json.getString("appid")
                        req.partnerId = json.getString("partnerid")
                        req.prepayId = json.getString("prepayid")
                        req.timeStamp = json.getString("timestamp")
                        req.packageValue = json.getString("package")
                        req.nonceStr = json.getString("noncestr")
                        req.sign = json.getString("sign")
                        req.extData = "app data" // optional
                        api!!.sendReq(req)
                        msg.obj = ""
                    }else{
                        msg.obj = "返回错误" + json.getString("retmsg")
                        //Toast.makeText(this@PayActivity, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show()
                    }
                }catch (e:Exception){
                    msg.obj = "异常：" + e.message
                    // Toast.makeText(this@PayActivity, "异常：" + e.message, Toast.LENGTH_SHORT).show()
                }
                hanlder.sendMessage(msg)
            }
        }).start()
    }

    private var hanlder: Handler = object: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                1-> if(msg.obj != null && msg.obj != ""){
                    mView!!.showMessage(msg.obj as String)
                }
            }
        }
    }
    override fun delWxPayResult() {
        quickCallJs("getPayResult",null)
    }
    private fun quickCallJs(method: String, callback: android.webkit.ValueCallback<String>?, vararg params: String) {
        val sb = StringBuilder()
        sb.append("javascript:" + method)
        if (params == null || params.isEmpty()) {
            sb.append("()")
        } else {
            sb.append("(").append(concat(*params)).append(")")
        }
        mView?.callJs(sb.toString(), callback)
    }

    private fun concat(vararg params: String): String {
        val mStringBuilder = StringBuilder()
        for (i in params.indices) {
            val param = params[i]
            if (!StringUtil.isJson(param)) {
                mStringBuilder.append("\"").append(param).append("\"")
            } else {
                mStringBuilder.append(param)
            }
            if (i != params.size - 1) {
                mStringBuilder.append(" , ")
            }
        }
        return mStringBuilder.toString()
    }

    override fun webShareResult(shareId: String) {
        quickCallJs("getCoinByApp",null,shareId)
    }

    override fun userShareGoldEnvelopes() {
        var user = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null) {
            ApiManager.userShareGoldEnvelopes(user.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                    .subscribe(
                            {
                                if(it.code == 2000){
                                    mView?.showMessage("${it.msg}，奖励${it.data}金币")
                                }

                            },
                            {
                                mView?.showMessage(it.message)
                            }
                    )
        }
    }

    override fun autoLogin(phone: String) {
        ApiManager.queryUserByPhone(phone)
                .map { it.data }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<User>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            SPUtil.saveUserBean(mView?.getContext(),it)
                            Consts.isLogined = true
                            Consts.user = it
                        },
                        {
                            Log.i("queryUserByPhone","出错"+it.message)
                        },{
                    mView?.hideProgressDialog()
                }
                )
    }
}