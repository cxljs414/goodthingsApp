package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/31
 * 修改内容：
 * 最后修改时间：
 */
data class GroupBuyDetailBean(
        @SerializedName("title") val title: String,
        @SerializedName("cover_url") val cover_url: String,
        @SerializedName("cId") val cId: Int,
        @SerializedName("virtual_num") val virtual_num: Int,
        @SerializedName("price") val price: Double,
        @SerializedName("spu_id") val spu_id: Int,
        @SerializedName("remark") val remark: String,
        @SerializedName("mer_id") val mer_id: Int,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("head_url") val head_url: String,
        @SerializedName("stock") val stock: Int,
        @SerializedName("old_price") val old_price: Double,
        @SerializedName("person_num") val person_num: Int
//
) {
}