package com.goodthings.app.base

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.EventBean
import com.goodthings.app.util.*
import com.goodthings.app.view.QRcodeDialog
import com.goodthings.app.view.SharePopwindow
import com.trello.rxlifecycle2.components.support.RxFragmentActivity
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
abstract class BaseActivity<in V:BaseView,T:BasePresenter<V>>: RxFragmentActivity(),BaseView {

    protected abstract var presenter:T
    protected lateinit var networkUtil: NetworkUtil
    //网络是否可用
    protected var isNetAvaiable = true
    //分享框
    protected var shareWindow: SharePopwindow? = null
    //显示alert
    protected var alertDialog:Dialog? = null

    private var progressDialog:Dialog? = null
    private var qrCodeDialog: QRcodeDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppUtil.addActivity(this)
        presenter.attachView(this as V)
        presenter.setLifeCycleProvider(this)
        networkUtil = NetworkUtil(this)
        isNetAvaiable= networkUtil.NETWORK_ENABLE
        EventBus.getDefault().register(this)
    }

    /**
     * 添加权限
     */
    fun addPermission(requestCode: Int,vararg permission: String){
        AndPermission.with(this)
                .requestCode(requestCode)
                .permission(*permission)
                .send()
    }

    /**
     * 添加权限成功后的操作，需重写
     */
    open fun addPermissionSuccess(requestCode: Int){

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AndPermission.onRequestPermissionsResult(this,requestCode,permissions,grantResults,object:PermissionListener{
            override fun onSucceed(requestCode: Int) {
                addPermissionSuccess(requestCode)
            }

            override fun onFailed(requestCode: Int) {
                showMessage("无此操作权限！")
            }

        })
    }

    override fun getActivity(): Activity =this

    override fun getContext(): Context =this

    override fun showError(error: String?) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show()
    }

    override fun showError(errorId: Int) {
        Toast.makeText(this,errorId,Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(msgId: Int) {
        Toast.makeText(this,msgId,Toast.LENGTH_SHORT).show()
    }

    override fun showProgressDialog(content: String) {
        progressDialog = MaterialDialog.Builder(this)
                .content(content)
                .progress(true, 0,false)
                .build()
        progressDialog?.show()
    }

    override fun showProgressDialog(content: Int) {
        progressDialog = MaterialDialog.Builder(this)
                .content(content)
                .progress(true, 0,false)
                .build()
        progressDialog?.show()
    }

    override fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    /**
     * 显示h5页面的alert
     */
    fun showAlert(msg:String?){
        alertDialog = null
        alertDialog = AlertDialog.Builder(this)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定"
                ) { dialog, _ ->
                    dialog.dismiss()
                }.create()
        alertDialog!!.show()
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun events(event:String){
        checkNetWork()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun events(payResult: EventBean<Int>){
        if(payResult.what == 1) {
            wxPayResult(payResult.content)
        }
    }

    override fun wxPayResult(content: Int) {

    }

    fun checkNetWork(){
        if(networkUtil.NETWORK_ENABLE){
            isNetAvaiable = true
            onNetwokChange(true)
        }else{
            isNetAvaiable = false
            onNetwokChange(false)
        }
    }

    /**
     * 网络变化后调用的方法，isenable网络是否可用
     */
    override fun onNetwokChange(isEnable: Boolean) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if(requestCode == 111){
            checkNetWork()
        }
    }

    fun openShareWindow(activity: Activity, view: View, url: String, title: String, description: String,imageRes:Int){
        val web = UMWeb(url)
        web.setThumb(UMImage(activity,imageRes))
        web.description = description
        web.title = title
        initShareWindow(activity,view,"1",web,"",null)
    }

    fun openShareWindow(activity: Activity, view: View, url: String, title: String, description: String,imageUrl:String){
        openShareWindow(activity,view,url,title,description,imageUrl,"0","",null)
    }

    fun openShareWindow(activity: Activity, view: View, url: String, title: String, description: String,imageUrl:String,callBack:UMShareListener){
        openShareWindow(activity,view,url,title,description,imageUrl,"0","",callBack)
    }

    /**
     * 显示分享框
     */
    fun openShareWindow(activity: Activity, view: View, url: String, title: String, description: String,imageUrl: String, type: String, json: String,callBack:UMShareListener?){
        val web = UMWeb(url)
        if(imageUrl == null || imageUrl.isEmpty()){
            web.setThumb(UMImage(activity,R.mipmap.icon_hsfs))
        }else{
            if(imageUrl.startsWith("http")){
                web.setThumb(UMImage(activity,imageUrl))
            }else{
                web.setThumb(UMImage(activity,Consts.IMAGEURL+imageUrl))
            }

        }
        web.description = description
        web.title = title

        initShareWindow(activity,view,type,web,json,callBack)
    }

    private fun initShareWindow(activity: Activity, view: View, type: String, web: UMWeb, json: String, callBack: UMShareListener?){
        shareWindow = null
        shareWindow = SharePopwindow(activity, View.OnClickListener {
            shareWindow?.dismiss()
            shareWindow?.backgroundAlpha(activity, 1f)
            if(type == "1"){
                shareWindow?.setQRcodeVisible(View.VISIBLE)
            }else{
                shareWindow?.setQRcodeVisible(View.GONE)
            }

            if(it.id == R.id.qrcode){
                showQRcodeDialog(json)
            }else{
                val type:SHARE_MEDIA = when(it.id){
                    R.id.weixinghaoyou -> SHARE_MEDIA.WEIXIN
                    R.id.pengyouquan -> SHARE_MEDIA.WEIXIN_CIRCLE
                    R.id.qqhaoyou -> SHARE_MEDIA.QQ
                    R.id.qqkongjian -> SHARE_MEDIA.QZONE
                    R.id.weibo -> SHARE_MEDIA.SINA
                    else -> SHARE_MEDIA.WEIXIN
                }
                share(activity,type,web,callBack)
            }

        })
        shareWindow?.showAtLocation(this,view,Gravity.BOTTOM,0,0)
    }

    /**
     * 显示二维码分享视图
     */
    private fun showQRcodeDialog(json: String) {
        qrCodeDialog = QRcodeDialog(this,json)
        qrCodeDialog?.show()
    }


    /**
     * 点击分享
     */
    private fun share(activity: Activity, type: SHARE_MEDIA, web: UMWeb, callBack: UMShareListener?) {
       var shareAction=ShareAction(activity)
               .withMedia(web)
                .setPlatform(type)
        if(callBack != null){
            shareAction.setCallback(callBack)
        }else{
            shareAction.setCallback(object : UMShareListener{
                override fun onResult(p0: SHARE_MEDIA?) {
                    showMessage("分享成功")
                    shareResult(true,"分享成功")
                }

                override fun onCancel(p0: SHARE_MEDIA?) {
                    shareResult(false,"取消分享")
                }

                override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                    showError(p1?.message)
                    shareResult(false,p1?.message!!)
                }

                override fun onStart(p0: SHARE_MEDIA?) {
                }

            })
        }
        shareAction.share()
    }

    override fun shareResult(isSuccess: Boolean, msg: String) {
        //showMessage(msg)
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    fun backgroundAlpha(context: Activity, bgAlpha: Float) {
        val lp = context.window.attributes
        lp.alpha = bgAlpha
        context.window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        context.window.attributes = lp
    }
    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
        alertDialog = null
        shareWindow?.dismiss()
        shareWindow = null
        qrCodeDialog?.dismiss()
        qrCodeDialog = null
        EventBus.getDefault().unregister(this)
    }
}