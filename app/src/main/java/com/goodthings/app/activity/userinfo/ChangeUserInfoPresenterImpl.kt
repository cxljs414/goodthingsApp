package com.goodthings.app.activity.userinfo

import android.graphics.BitmapFactory
import android.net.Uri
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.bean.BaseResult
import com.goodthings.app.bean.CommonResult
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.AppUtil
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/7
 * 修改内容：
 * 最后修改时间：
 */
class ChangeUserInfoPresenterImpl:
        BasePresenterImpl<ChangeUserInfoContract.ChangeUserInfoVew>(),
        ChangeUserInfoContract.ChangeUserInfoPresenter{
    override fun isUpdateUserName() {
        ApiManager.isUpdateUserName(Consts.user?.id!!)
                .compose(RxUtil.hanlderBaseResult())
                .compose(lifecycle?.bindUntilEvent<Boolean>(ActivityEvent.DESTROY))
                .doOnError {  }
                .subscribe({
                    mView?.canEditNickname(it)
                })
    }

    override fun updateUserInfo(tempHeadImage: String, nickName: String, sexkey: Int, ageKey: Int) {
        mView?.showProgressDialog(R.string.saveing)
        ApiManager.updateUserInfo(Consts.user?.id!!,
                sexkey,nickName,tempHeadImage,ageKey,Consts.user?.phone!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(lifecycle?.bindUntilEvent<CommonResult>(ActivityEvent.DESTROY))
                .doOnError { mView?.hideProgressDialog() }
                .subscribe({
                    mView?.hideProgressDialog()
                    if(it.code == 2000){
                        var user= SPUtil.getUserBean(mView?.getContext()!!)
                        user?.head_url = Consts.IMAGEURL+tempHeadImage
                        user?.nickname = nickName
                        user?.age_range = ageKey
                        user?.sex_key = sexkey
                        SPUtil.saveUserBean(mView?.getContext(),user)
                        Consts.user = user
                        mView?.goBack()
                    }else{
                        mView?.showMessage(it.msg)
                    }

                },{
                    mView?.hideProgressDialog()
                })
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
                                mView?.updateTempHeadUrl(it.data)
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