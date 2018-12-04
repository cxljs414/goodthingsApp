package com.goodthings.app.application

import android.support.multidex.MultiDexApplication
import com.goodthings.app.http.ApiManager
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
class MyApplication : MultiDexApplication() {
    //各个平台的配置，建议放在全局Application或者程序入口

    override fun onCreate() {
        super.onCreate()
        //16f5417bfdf8d3804c1ac6f160ee89f6
        PlatformConfig.setWeixin("wx1279f46d84bd1fba", "a399c849760ca5dc5865b9f2dfc7bc6b")
        PlatformConfig.setQQZone("1106024685", "u4BBSdLWEejNlXuy")
        PlatformConfig.setSinaWeibo("613946863","374b8dff8fd61e0d1264cd7d2d04979c","http://android.myapp.com/myapp/detail.htm?apkName=com.goodthings.app")
        UMShareAPI.get(this)
        ApiManager.setContext(this)
    }

}