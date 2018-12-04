package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/4
 * 修改内容：
 * 最后修改时间：
 */
data class GroupListBean(
        @SerializedName("title") val title: String,
        @SerializedName("cover_url") val cover_url: String,
        @SerializedName("cId") val cId: Int,
        @SerializedName("virtual_num") val virtual_num: Int,
        @SerializedName("price") val price: String,
        @SerializedName("old_price") val old_price: String
) {
}