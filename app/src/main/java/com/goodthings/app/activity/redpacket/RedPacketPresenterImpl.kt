package com.goodthings.app.activity.redpacket

import android.util.Log
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.User
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.apache.http.NameValuePair
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicNameValuePair
import org.apache.http.protocol.HTTP
import java.io.IOException
import java.io.UnsupportedEncodingException


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/4
 * 修改内容：
 * 最后修改时间：
 */
class RedPacketPresenterImpl :
        BasePresenterImpl<RedPacketContract.RedPacketView>(),
        RedPacketContract.RedPacketPresenter{
    override fun dailyRedEnvelopes() {
        var user: User? = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null) {

            ApiManager.dailyRedEnvelopes(user?.id)
                    //.compose(RxUtil.hanlderCommResult())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                    .subscribe(
                            {
                                if(it == null)return@subscribe
                                if(it.code == 2000 && it.data != null){
                                    mView?.showHongBao(true,it.data!!)
                                }else{
                                    mView?.showHongBao(true,it.msg)
                                }
                            },
                            {
                                mView?.showHongBao(false,it.message!!)
                            },
                            {
                            }
                    )
        }
    }

}