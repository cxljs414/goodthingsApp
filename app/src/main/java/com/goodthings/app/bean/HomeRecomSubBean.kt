package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/7
 * 修改内容：
 * 最后修改时间：
 */
data class HomeRecomSubBean(
        @SerializedName("cateId") val cateId: Int,
        @SerializedName("collectNum") val collectNum: Int,
        @SerializedName("content") val content: String,
        @SerializedName("contentType") val contentType: Int,
        @SerializedName("coverPic") val coverPic: String,
        @SerializedName("headUrl") val headUrl: String,
        @SerializedName("linkUrl") val linkUrl: String,
        @SerializedName("locateType") val locateType: Int,
        @SerializedName("nickName") val nickName: String,
        @SerializedName("recId") val recId: Int,
        @SerializedName("relateId") val relateId: Int,
        @SerializedName("sort") val sort: Int,
        @SerializedName("title") val title: String,
        @SerializedName("userId") val userId: Int,
        @SerializedName("isFocus") var isFocus: Int = 0,//0未关注
        @SerializedName("colOldPrice") val colOldPrice: String,
        @SerializedName("colPrice") val colPrice: String,
        @SerializedName("colSaleNum") val colSaleNum: Int
        //
) :Serializable{
}