package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/2
 * 修改内容：
 * 最后修改时间：
 */
class WxPayInitBean(
        @SerializedName("appid") val appid:String,
        @SerializedName("noncestr") val noncestr:String,
        @SerializedName("timestamp") val timestamp:String,
        @SerializedName("partnerid") val partnerid:String,
        @SerializedName("prepayid") val prepayid:String
        //package = "Sign=WXPay"
        ) {
}