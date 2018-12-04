package com.goodthings.app.activity.login

import android.content.Intent
import android.util.Log
import com.goodthings.app.R
import com.goodthings.app.activity.verifcode.VerifCodeActivity
import com.goodthings.app.activity.verifcode.VerifCodeContract
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.bean.User
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.SPUtil
import com.google.gson.Gson
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/2/7
 * 修改内容：
 * 最后修改时间：
 */
class LoginPresenterImpl : BasePresenterImpl<LoginContract.LoginView>(), LoginContract.LoginPresenter{
    override fun setFromHongbao(fromHongBao: Boolean) {
        isFromHongBao = fromHongBao
    }

    var isUserExist:Boolean = false
    var isFromHongBao:Boolean = false
    var temUser:User? = null

    override fun queryUserByPhone(phone: String) {
        if(temUser?.phone == phone){
            mView?.setShowUser(temUser?.nickname,temUser?.head_url)
            return
        }
        mView?.showProgressDialog(R.string.login_queryuser)
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
                            isUserExist = true
                            temUser = it
                            mView?.setShowUser(it.nickname,it.head_url)
                        },
                        {
                            isUserExist = false
                            Log.i("queryUserByPhone","出错"+it.message)
                        },{
                            mView?.hideProgressDialog()
                        }
                )
    }

    override fun loginByPhone(phone: String) {
        if(isUserExist){
            goToVerifCode(phone,VerifCodeContract.TYPE_VERIFCODE)
        }else{
            goToVerifCode(phone,VerifCodeContract.TYPE_FIRSTREGIST)
        }
    }

    override fun loginbyPsd(phone: String, psd: String) {
        mView?.showProgressDialog(R.string.logining)
        ApiManager.loginbyPassword(phone,psd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it.code == 2000){
                                mView?.hideProgressDialog()
                                SPUtil.saveUserBean(mView?.getContext(),temUser)
                                Consts.isLogined = true
                                Consts.user = temUser
                                mView?.goBack(temUser!!.id,it.data)
                            }else{
                                mView?.showMessage(it.msg)
                            }
                        },
                        {
                        },
                        {
                            mView?.hideProgressDialog()
                        }
                )
    }

    override fun forgetPassword(phone: String) {
        if(isUserExist){
            goToVerifCode(phone,VerifCodeContract.TYPE_FORGETPASSWORD)
        }else{
            mView?.showMessage("手机号未注册，请先注册")
        }
    }

    private fun goToVerifCode(phone: String,type:Int) {
        var intent = Intent(mView?.getContext(), VerifCodeActivity::class.java)
        intent.putExtra("phone",phone)
        intent.putExtra("type",type)
        intent.putExtra("user", Gson().toJson(temUser))
        intent.putExtra("isFromHongBao",isFromHongBao)
        mView?.getContext()?.startActivity(intent)
        mView?.getActivity()?.finish()
    }
}