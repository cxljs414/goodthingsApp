package com.goodthings.app.activity.main

import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import com.amap.api.services.core.PoiItem
import com.goodthings.app.activity.redpacket.RedPacketActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.goodthings.app.util.StringUtil
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.io.File

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/26
 * 修改内容：
 * 最后修改时间：
 */
class MainPresenterImpl : BasePresenterImpl<MainContract.MainView>(), MainContract.MainPresenter {

    private var mImagePath: String? = ""
    lateinit var api: IWXAPI
    override fun start() {
        api = WXAPIFactory.createWXAPI(mView?.getContext(),null)
        api.registerApp(Consts.WXAPPID)
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

    override fun loadNewPage(s: String) {
        quickCallJs("footerSelect",null,s)
    }

    /**
     * 调用微信
     */
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

    override fun delWxPayResult(){
        quickCallJs("getPayResult",null)
    }

    override fun handleActivityResult(requestCode: Int, data: Intent?) {

        when(requestCode){
            Consts.REQUEST_CODE_GOMAP ->{
                val poi: PoiItem = data!!.getParcelableExtra("selectPoi")
                quickCallJs("showLocation",null,poi.title)
            }
            Consts.REQUEST_LOGIN -> {
                handleLoginResult(true,data)
            }
            Consts.REQUEST_SELECT_PIC ->{
                handleCropResult(data)
            }
            Consts.REQUEST_CITY -> {
                var city= data?.getStringExtra("cityName")
                var code= data?.getStringExtra("cityCode")

                city?.let {
                    goNextPage("notifyCityChange",city)
                }
                mView?.getContext()?.let {
                    var localCode = SPUtil.getStringValue(mView?.getContext()!!,"cityCode","")
                    if(code != localCode) {
                        requestCityCode(city!!)
                    }else{
                        mView?.notifyCityChanged(city,false)
                    }
                }

            }
        }
    }

    override fun handleLoginResult(loginSuccess: Boolean, data: Intent?) {
        if(loginSuccess){
            retryLogin()
            var userId:String= data?.getStringExtra("userId")!!
            var isFromHongbao=data?.getBooleanExtra("isFromHongBao",false)
            if(isFromHongbao){
                tyDailyRedEnvelopes(userId.toInt())
            }
        }else{
            quickCallJs("cancelLogin",null)
        }
    }

    override fun retryLogin() {
        Log.i("main","retryLogin")
        var user = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null){
            Log.i("main","toSynLogin")
            quickCallJs("toSynLogin",null,"${user.id}")
        }
    }

    override fun requestCityCode(city: String) {
        ApiManager.getCityCode(city)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<String>(ActivityEvent.DESTROY))
                .subscribe(
                        {
                            var localCode = SPUtil.getStringValue(mView?.getContext()!!,"cityCode","")
                            if(it != localCode) {
                                SPUtil.putStringValue(mView?.getContext()!!, "cityCode", it)
                                SPUtil.putStringValue(mView?.getContext()!!,"city",city)
                                mView?.notifyCityChanged(city,true)
                            }else{
                                mView?.notifyCityChanged(city,false)
                            }
                        },
                        {
                            mView?.showMessage(it.message)
                            mView?.notifyCityChanged(city,false)
                        },
                        {
                        }
                )
    }

    override fun menuHasNewMsg() {
        quickCallJs("getLocalMsgCount",null)
    }

    override fun dealMsgCount(count: String) {
        ApiManager.menuHasNewMsg()
                .compose(RxUtil.hanlderBaseResult())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<Int>(ActivityEvent.DESTROY))
                .subscribe(
                        {
                            //var localCount=SPUtil.getIntValue(mView?.getContext()!!,"msgCount",0)
                            if(it > count.toInt()){
                                mView?.notifyHasNewMsgs(true)
                                SPUtil.putIntValue(mView?.getContext()!!,"msgCount",it)
                            }
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                        }
                )
    }

    override fun goNextPage(pageName: String, params: String) {
        if(params.isEmpty()){
            quickCallJs("goNextPage",null,pageName)
        }else{
            quickCallJs("goNextPage",null,pageName,params)
        }
    }

    override fun tyDailyRedEnvelopes(userId: Int) {
        ApiManager.tyDailyRedEnvelopes(userId.toInt())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .subscribe(
                        {
                            if (it.code == 2000) {
                                mView?.getActivity()?.startActivity(Intent(mView?.getContext(), RedPacketActivity::class.java))
                            } else {
                                var intent = Intent(mView?.getContext(), RedPacketActivity::class.java)
                                intent.putExtra("hongbao",it.msg)
                                mView?.getActivity()?.startActivity(intent)
                            }
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                        }
                )
    }

    override fun destroy() {
        deleteDir(Consts.SAVEPICPATH)
        deleteDir(Consts.APPSDCARDPATH)
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

    override fun shareGoldEnvelopes(shareId: String) {
        var user = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null) {
            ApiManager.shareGoldEnvelopes(user.id,shareId.toInt(),2)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                    .subscribe(
                            {
                                if(it.code == 2000){
                                    mView?.showShareGold()
                                    //share(shareId)
                                    //mView?.showMessage("${it.msg}，奖励${it.data}金币")
                                }

                            },
                            {
                                mView?.showMessage(it.message)
                            }
                    )
        }
    }

    override fun share(shareId: String) {
        var user = SPUtil.getUserBean(mView?.getContext()!!)
        /*if(user != null) {
            ApiManager.share(user.id,shareId.toInt())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                    .subscribe(
                            {

                            },
                            {
                            }
                    )
        }*/
    }

    override fun saveUserInfo(nickName: String?, revisesSex: Int, headUrl: String?, revisesAge: Int) {
        quickCallJs("appUserInfoSave",null,
                Consts.user?.id.toString(),nickName!!,"$revisesSex",headUrl!!,"$revisesAge")
    }

    //------------------本地方法--------------------//

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

    /**
     * 处理裁剪结果
     */
    private fun handleCropResult(intent: Intent?) {
        if(mImagePath != ""){
            deleteTempPhotoFile()
        }
        mView?.getPicSuccess(arrayOf(intent?.data!!))
    }

    /**
     * 删除临时文件
     */
    private fun deleteTempPhotoFile() {
        val file = File(mImagePath)
        if(file.exists() && file.isFile){
            file.delete()
        }
        mImagePath = ""
    }

    private fun deleteDir(dir:String?){
        val file = File(dir)
        if(file.exists()){
            for(file:File in file.listFiles()){
                if(file.name.endsWith(".jpg") || file.name.endsWith(".png")){
                    file.delete()
                }
            }
        }
    }

}
