package com.goodthings.app.activity.regist

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import com.goodthings.app.R
import com.goodthings.app.activity.registHongbao.RegistHongBaoActivity
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.BaseResult
import com.goodthings.app.bean.RegistBean
import com.goodthings.app.bean.RegistUserBean
import com.goodthings.app.bean.User
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.AppUtil
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import com.umeng.socialize.sina.helper.MD5
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/2/8
 * 修改内容：
 * 最后修改时间：
 */
class RegistPresenter : BasePresenterImpl<RegistContract.RegistView>(),RegistContract.RegistPresenter {

    private var registBean:RegistBean? = null

    override fun start() {
        val phone = mView?.getActivity()?.intent?.getStringExtra("phone")
        var password = mView?.getActivity()?.intent?.getStringExtra("password")
        registBean = RegistBean(phone!!, "","","2","1",password!!)
    }

    override fun setSex(sex: String) {
        registBean?.sexKey = sex
    }

    override fun setAge(age: String) {
        registBean?.age = age
    }

    override fun commitRegist(nickname: String) {
        if(registBean?.headurl == null || registBean?.headurl!!.isEmpty()){
            mView?.showMessage("请上传头像")
            return
        }
        if(nickname.isEmpty()){
            mView?.showMessage("请输入昵称")
            return
        }

        if(!Regex("^[\\u4e00-\\u9fa5_a-zA-Z0-9]+\$").matches(nickname)){
            mView?.showMessage("昵称内容不能包含特殊字符")
            return
        }

        var md5Psw = MD5.hexdigest(registBean?.passwod)
        mView?.showProgressDialog(R.string.saveing)
        ApiManager.registPsersonInfo(registBean?.headurl!!,nickname,registBean?.sexKey,registBean?.age,registBean?.phone,md5Psw)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<RegistUserBean>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            SPUtil.saveUserBean(mView?.getContext(),it.user)
                            var intent = Intent(mView?.getActivity(), RegistHongBaoActivity::class.java)
                            intent.putExtra("hongbao",it.aucont)
                            mView?.getActivity()?.startActivity(intent)
                            mView?.getActivity()?.finish()
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                            mView?.hideProgressDialog()
                        }
                )

    }

    override fun uploadImage(data: Uri?) {
        if(data == null)return
        val imgString= AppUtil.Bitmap2StrByBase64(BitmapFactory.decodeFile(data.path))
        mView?.showProgressDialog(R.string.uploadheadpic)
        ApiManager.uploadimage(imgString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<BaseResult<String>>(ActivityEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it.code == 2000){
                                registBean?.headurl = it.data
                                mView?.showHeadPic(it.data)
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
}