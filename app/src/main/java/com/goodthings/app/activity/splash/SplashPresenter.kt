package com.goodthings.app.activity.splash

import android.util.Log
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.GuideBean
import com.goodthings.app.bean.UpdateAppBean
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.AppUtil
import com.goodthings.app.application.Consts
import com.goodthings.app.util.FileCallBack
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
class SplashPresenter :
        BasePresenterImpl<SplashContract.SplashView>(),
        SplashContract.SplashPresenter {

    override fun requestGuide() {
        ApiManager.requestGuide()
                .map {
                    it.data
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<GuideBean>(ActivityEvent.DESTROY))
                .subscribe({
                    mView?.goToMain(it)
                },{
                    //mView?.showMessage(it.toString())
                    mView?.goToMain(null)
                })

    }

    override fun checkVersion() {
        ApiManager.checkVersion()
                .map {
                        it.data
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<UpdateAppBean>(ActivityEvent.DESTROY))
                .subscribe(
                {
                    mView?.updateVersion(it)
                },{
                    mView?.updateVersion(null)
                    Log.i("update","检查更新出错")
                })
    }

    override fun cancelUpdate(isSuccess:Boolean,forceUpdate: Int) {
        if (isSuccess) {
            AppUtil.exitApp()
        } else{
            if (forceUpdate == 1) {
                AppUtil.exitApp()
            }else{
                requestGuide()
            }
        }
    }

    override fun dealUpdate(url: String,newVersion:Int) {

        //先判断本地是否已经有了apk文件，并且版本等于要更新的版本
        //有的话直接安装本地的apk文件，没有再重新下载
        val fileDir= File(Consts.APPSDCARDPATH)
        if(!fileDir.exists()){
            fileDir.mkdirs()
        }
        val file = File(Consts.APPSDCARDPATH, "goodthings.apk")
        if(file.exists() && AppUtil.apkVersion(mView?.getContext()!!,"${Consts.APPSDCARDPATH}/goodthings.apk") == newVersion){
            AppUtil.installApk(mView?.getContext()!!, File(Consts.APPSDCARDPATH, "goodthings.apk"))
            AppUtil.exitApp()
        }else{

            val callBack = object : FileCallBack<ResponseBody>(Consts.APPSDCARDPATH, "goodthings.apk") {

                override fun onSuccess(responseBody: ResponseBody) {
                    SPUtil.putBooleanValue(mView?.getContext()!!,"isApkUpdated",true)
                    AppUtil.installApk(mView?.getContext()!!, File(Consts.APPSDCARDPATH, "goodthings.apk"))
                    AppUtil.exitApp()
                }

                override fun progress(progress: Long, total: Long) {
                    mView?.updateProgress(progress,total)
                }

                override fun onStart() {
                    mView?.shoProgress()
                }

                override fun onCompleted() {
                    mView?.hideProgress()
                }

                override fun onError(e: Throwable) {
                    //TODO: 对异常的一些处理
                    e.printStackTrace()
                }
            }
            ApiManager.downLoadAPK(url)
                    .onBackpressureBuffer()
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        callBack.onStart()
                    }
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .doOnNext {
                        callBack.saveFile(it)
                    }
                    .compose(lifecycle?.bindUntilEvent<ResponseBody>(ActivityEvent.DESTROY))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe (
                            { callBack.onSuccess(it) },
                            { callBack.onError(it) },
                            { callBack.onCompleted() }
                    )
        }
    }

}

