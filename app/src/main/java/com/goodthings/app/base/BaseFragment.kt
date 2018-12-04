package com.goodthings.app.base

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.goodthings.app.R
import com.goodthings.app.util.NetworkUtil
import com.goodthings.app.view.SharePopwindow
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import com.umeng.socialize.ShareAction
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA
import com.umeng.socialize.media.UMImage
import com.umeng.socialize.media.UMWeb
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/2
 * 修改内容：
 * 最后修改时间：
 */
abstract class BaseFragment<in V:BaseView,T:BasePresenter<V>> : RxFragment(),BaseView{

    protected abstract var presenter:T
    private var progressDialog: Dialog? = null
    protected lateinit var networkUtil: NetworkUtil
    //网络是否可用
    protected var isNetAvaiable = true
    //分享框
    protected var shareWindow: SharePopwindow? = null
    //显示alert
    protected var alertDialog:Dialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this as V)
        presenter.setFragmentLifeCycleProvider(this)
        networkUtil = NetworkUtil(context!!)
        isNetAvaiable= networkUtil.NETWORK_ENABLE
        //EventBus.getDefault().register(this)
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
        AndPermission.onRequestPermissionsResult(this,requestCode,permissions,grantResults,object: PermissionListener {
            override fun onSucceed(requestCode: Int) {
                addPermissionSuccess(requestCode)
            }

            override fun onFailed(requestCode: Int) {
                showMessage("无此操作权限！")
            }

        })
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

    override fun showError(error: String?) {
        Toast.makeText(context,error, Toast.LENGTH_SHORT).show()
    }

    override fun showError(errorId: Int) {
        Toast.makeText(context,errorId, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(msgId: Int) {
        Toast.makeText(context,msgId, Toast.LENGTH_SHORT).show()
    }

    override fun showProgressDialog(content: String) {
        progressDialog = MaterialDialog.Builder(context!!)
                .content(content)
                .progress(true, 0,false)
                .build()
        progressDialog?.show()
    }

    override fun showProgressDialog(content: Int) {
        progressDialog = MaterialDialog.Builder(context!!)
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
        alertDialog = AlertDialog.Builder(context)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("确定"
                ) { dialog, _ ->
                    dialog.dismiss()
                }.create()
        alertDialog!!.show()
    }

    /**
     * 网络变化后调用的方法，isenable网络是否可用
     */
    override fun onNetwokChange(isEnable: Boolean) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 111){
            checkNetWork()
        }
    }

    /**
     * 显示分享框
     */
    fun openShareWindow(activity: Activity, view: View, url:String, title:String, description:String){
        if(shareWindow == null){
            shareWindow = SharePopwindow(activity, View.OnClickListener {
                shareWindow?.dismiss()
                shareWindow?.backgroundAlpha(activity, 1f)
                val type: SHARE_MEDIA = when(view.id){
                    R.id.weixinghaoyou -> SHARE_MEDIA.WEIXIN
                    R.id.pengyouquan -> SHARE_MEDIA.WEIXIN_CIRCLE
                    R.id.qqhaoyou -> SHARE_MEDIA.QQ
                    R.id.qqkongjian -> SHARE_MEDIA.QZONE
                    R.id.weibo -> SHARE_MEDIA.SINA
                    else -> SHARE_MEDIA.WEIXIN
                }
                share(activity,type,url,title,description)
            })
        }
        shareWindow?.showAtLocation(activity,view, Gravity.BOTTOM,0,0)
    }

    /**
     * 点击分享
     */
    private fun share(activity: Activity, type: SHARE_MEDIA, url: String, title: String, description: String) {
        val web = UMWeb(url)
        web.setThumb(UMImage(activity, R.mipmap.icon_hsfs))
        web.description = description
        web.title = title
        ShareAction(activity).withMedia(web)
                .setPlatform(type)
                .setCallback(object : UMShareListener {
                    override fun onResult(p0: SHARE_MEDIA?) {
                        showMessage("分享成功")
                    }

                    override fun onCancel(p0: SHARE_MEDIA?) {
                    }

                    override fun onError(p0: SHARE_MEDIA?, p1: Throwable?) {
                        showError("分享失败")
                    }

                    override fun onStart(p0: SHARE_MEDIA?) {
                    }

                }).share()
    }

    override fun shareResult(isSuccess: Boolean, msg: String) {
    }

    override fun wxPayResult(content: Int) {
    }


    override fun onDestroy() {
        super.onDestroy()
        alertDialog?.dismiss()
        alertDialog = null
        shareWindow?.dismiss()
        shareWindow = null
        EventBus.getDefault().unregister(this)
    }

}