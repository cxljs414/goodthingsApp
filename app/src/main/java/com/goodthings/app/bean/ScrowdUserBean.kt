package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/27
 * 修改内容：
 * 最后修改时间：
 */
data class ScrowdUserBean(
        @SerializedName("userName") val userName: String,
        @SerializedName("img") val img: String,
        @SerializedName("count") val count: Int,
        @SerializedName("spuCount") val spuCount: Int,
        @SerializedName("city") val city: String,
        @SerializedName("browse") val browse: Boolean
        ) {
}