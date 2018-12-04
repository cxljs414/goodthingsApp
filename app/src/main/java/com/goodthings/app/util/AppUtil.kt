package com.goodthings.app.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.view.WindowManager
import java.io.File
import android.graphics.Bitmap.CompressFormat
import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
object AppUtil {

    private val activities= ArrayList<Activity>()

    fun addActivity(activity: Activity){
        activities.add(activity)
    }

    fun exitApp(){
        for(act in activities){
            act?.finish()
        }
    }
    fun getVersionCode(context: Context):Int{
        var code = -1
        try {
            val packInfo = context.packageManager.getPackageInfo(context.packageName,0)
            code = packInfo.versionCode
        }catch (e:Exception){

        }
        return code
    }

    fun installApk(context: Context, file: File) {
        val intent = Intent()
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.action = Intent.ACTION_VIEW
        val uri: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(context, context.packageName + ".fileProvider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        } else {
            uri = Uri.fromFile(file)
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive")
        context.startActivity(intent)
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

    fun apkVersion(context: Context,path:String):Int{
        val pm = context.packageManager
        val info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES)
        if (info != null) {
            return info.versionCode
        }
        return -1
    }


    fun Bitmap2StrByBase64(bit: Bitmap): String {
        val bos = ByteArrayOutputStream()
        bit.compress(CompressFormat.JPEG, 100, bos)//参数100表示不压缩
        val bytes = bos.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}