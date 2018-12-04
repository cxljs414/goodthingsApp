package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/12
 * 修改内容：
 * 最后修改时间：
 */
data class ShareParamBean(
        @SerializedName("collageId")val collageId:Int,
        @SerializedName("count")val count:Int,
        @SerializedName("tId")val tId:Int
) {
}