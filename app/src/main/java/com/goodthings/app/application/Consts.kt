package com.goodthings.app.application

import com.goodthings.app.bean.CityBean
import com.goodthings.app.bean.HomeRecomSubBean
import com.goodthings.app.bean.User

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
object Consts {
    const val URL = "http://wechat.higoodthings.com/"
    //const val URL = "http://test.higoodthings.com/"
    //const val URL = "http://192.168.0.127:8082/"
    //const val URL = "http://192.168.0.108:8082/"
    //const val URL = "http://192.168.0.126:8082/"
    //const val URL = "http://192.168.0.250:8082/"

    //图片服务器地址
    const val IMAGEURL = "http://imgpb.higoodthings.com/"
    //临时图片存储地址
    const val SAVEPICPATH:String = "mnt/sdcard/goodthings/temppic"
    //一般文件存储地址
    const val APPSDCARDPATH:String = "mnt/sdcard/goodthings"
    //一般图片文件存储地址
    const val APPIMAGEPATH:String = "mnt/sdcard/goodthings/pictures"
    //微信id
    const val WXAPPID = "wx1279f46d84bd1fba"

    //跳转activity的人requestCode
    const val SELECT_PIC_BY_TACK_PHOTO = 104
    const val SELECT_PIC_BY_PICK_PHOTO = 105
    const val REQUEST_CODE_GOMAP = 106
    const val REQUEST_SELECT_PIC: Int = 107
    const val REQUEST_ADVER = 108
    const val REQUEST_LOGIN = 109
    const val REQUEST_CITY = 110
    const val REQUEST_ORDER_PAY = 111
    const val REQUEST_ADDADDR = 112
    const val REQUEST_CFORDER_DETAIL = 113
    const val REQUEST_ADDRLIST = 114
    const val REQUEST_GPORDER_DETAIL = 115
    const val REQUEST_CHANGEUSERINFO = 116
    const val REQUEST_ADDCOMMENT = 117

    //展现样式0无图1右单图2大单图3小广告图4大广告图
    const val MAIN_TYPE_DEFAULT = 1
    const val MAIN_TYPE_NOIMAGE = 0
    const val MAIN_TYPE_BIGIMAGE = 2
    const val MAIN_TYPE_SMALLADVER = 3
    const val MAIN_TYPE_BIGADVER = 4
    const val MAIN_TYPE_TOPIC = 5
    const val MAIN_TYPE_PERSON = 6

    //变量
    var isLogined = false
    var user:User? = null
    var cityList:MutableList<CityBean>? = null
    var prodCrowdId = -1
}