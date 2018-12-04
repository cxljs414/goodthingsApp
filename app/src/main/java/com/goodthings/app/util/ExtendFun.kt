package com.goodthings.app.util

import android.content.Context
import android.content.pm.PackageManager
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.jakewharton.rxbinding2.widget.RxRadioGroup
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/5
 * 修改内容：
 * 最后修改时间：
 */

fun EditText.textChange(callBack:(str:String)->Unit){
    RxTextView.afterTextChangeEvents(this)
            .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> callBack(it.editable()!!.toString()) }
}

fun RadioGroup.checkChange(callBack:(position:Int) ->Unit){
    RxRadioGroup.checkedChanges(this)
            .subscribe({ callBack(it) })
}

fun Context.toast(content:String){
    Toast.makeText(this,content,Toast.LENGTH_SHORT).show()
}

fun Context.toast(resId:Int){
    Toast.makeText(this,resId,Toast.LENGTH_SHORT).show()
}

fun String.checkPackage(mContext:Context):Boolean{
    if(this.isEmpty()){
        return false
    }

    return try {
        mContext.packageManager.getApplicationInfo(this,PackageManager.GET_UNINSTALLED_PACKAGES)
        true
    }catch (e:Exception){
        false
    }


}

fun View.onClick(callback:(view:View)->Unit){
    this.setOnClickListener { callback(it) }
}

fun Int.visible(helper:BaseViewHolder?,visible:Boolean){
    helper?.setVisible(this,visible)
}
