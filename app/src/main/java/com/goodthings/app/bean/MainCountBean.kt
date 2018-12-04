package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/14
 * 修改内容：
 * 最后修改时间：
 */
data class MainCountBean(
        @SerializedName("sum")val sum:String,//钱
        @SerializedName("count")val count:String,//成团数
        @SerializedName("max")val max:String//人数
) {
}