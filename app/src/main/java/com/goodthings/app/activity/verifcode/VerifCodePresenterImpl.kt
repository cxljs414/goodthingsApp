package com.goodthings.app.activity.verifcode

import android.content.Intent
import com.goodthings.app.R
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.redpacket.RedPacketActivity
import com.goodthings.app.activity.regist.RegistActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.BaseResult
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
class VerifCodePresenterImpl : BasePresenterImpl<VerifCodeContract.VerifCodeView>(),VerifCodeContract.VerifCodePresenter{

    var type:Int? = VerifCodeContract.TYPE_VERIFCODE
    var phone:String? = null
    var code:String? = null
    var temUser:User? = null
    override fun start() {
        val data:Intent?= mView?.getActivity()?.intent
        type = data?.getIntExtra("type",VerifCodeContract.TYPE_VERIFCODE)
        phone = data?.getStringExtra("phone")
        var userjson = data?.getStringExtra("user")
        temUser = Gson().fromJson(userjson,User::class.java)
        mView?.initView(phone,type)
        sendVerifCode()
    }

    /**
     * 发送验证码
     */
    override fun sendVerifCode() {
        mView?.showProgressDialog(R.string.sendVerifCode)
        ApiManager.sendVerifCode(phone!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<BaseResult<String>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it.code == 2000){
                                code = it.data
                            }else{
                                mView?.showMessage(it.msg)
                            }
                        },
                        {
                        },{
                    mView?.hideProgressDialog()
                }
                )
    }

    override fun commit(verifCode: String, password: String, type: Int?) {
        if(verifCode != code){
            mView?.showMessage("验证码错误")
            return
        }
        when(type){
            VerifCodeContract.TYPE_VERIFCODE ->{
                //保存用户信息到sp
                SPUtil.saveUserBean(mView?.getContext(),temUser)
                Consts.isLogined = true
                Consts.user = temUser
                var isFromHongBao= mView?.getActivity()?.intent?.getBooleanExtra("isFromHongBao",false)
                if(isFromHongBao!!){
                    tyDailyRedEnvelopes(temUser?.id!!)
                }else{
                    mView?.getActivity()?.finish()
                }
            }
            VerifCodeContract.TYPE_FIRSTREGIST ->{
                //调用注册接口
                firstRegist(phone,password)
            }
            VerifCodeContract.TYPE_FORGETPASSWORD ->{
                //修改密码接口
                updatePassword(password)
            }
        }
    }

    private fun firstRegist(phone: String?, password: String) {
        mView?.showProgressDialog(R.string.registing)
        if (phone != null) {
            ApiManager.firstRegist(phone,password)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                    .doOnError { mView?.hideProgressDialog() }
                    .subscribe(
                            {
                                mView?.showMessage(it.msg)
                                if(it.code == 2000){
                                    var intent=Intent(mView?.getContext(),RegistActivity::class.java)
                                    intent.putExtra("phone",phone)
                                    intent.putExtra("password",password)
                                    mView?.getContext()?.startActivity(intent)
                                    mView?.getActivity()?.finish()
                                }
                            },
                            {
                            },{
                        mView?.hideProgressDialog()
                    }
                    )
        }
    }

    private fun updatePassword(newPsd:String){
        mView?.showProgressDialog(R.string.updatepsd)
        ApiManager.updatePassword(phone!!,newPsd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it.code == 2000){
                                mView?.showMessage(it.msg)
                                mView?.getContext()?.startActivity(Intent(mView?.getContext(),LoginActivity::class.java))
                                mView?.getActivity()?.finish()
                            }else{
                                mView?.showMessage(it.msg)
                            }
                        },
                        {
                        },{
                    mView?.hideProgressDialog()
                }
                )
    }

    private fun tyDailyRedEnvelopes(userId: Int) {
        ApiManager.tyDailyRedEnvelopes(userId)
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
                            mView?.getActivity()?.finish()
                        },
                        {
                            mView?.showMessage(it.message)
                            mView?.getActivity()?.finish()
                        },
                        {
                        }
                )
    }
}