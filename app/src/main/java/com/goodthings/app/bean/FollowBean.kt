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
data class FollowBean(
        @SerializedName("code_like") val code_like: String,
        @SerializedName("code_collect") val code_collect: String,
        @SerializedName("code_follow") val code_follow: String
        ) {
}