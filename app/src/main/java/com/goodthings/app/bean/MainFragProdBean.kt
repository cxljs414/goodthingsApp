package com.goodthings.app.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/26
 * 修改内容：
 * 最后修改时间：
 */
data class MainFragProdBean (
        @SerializedName("collectCount") val collectCount:Int,
        @SerializedName("coverUrl") val coverUrl:String,
        @SerializedName("headUrl") val headUrl:String,
        @SerializedName("id") val id:Int,
        @SerializedName("nickName") val nickName:String,
        @SerializedName("prdTitle") val prdTitle:String,
        @SerializedName("type") var type:Int,//展现样式0无图1右单图2大单图3小广告图4大广告图
        @SerializedName("userId") val userId:Int,
        @SerializedName("viewCount") val viewCount:Int,
        @SerializedName("prdName") val prdName:String,

        //非固定部分需要的字段
        @SerializedName("collectNum") val collectNum:Int,
        @SerializedName("content") val content:String,
        @SerializedName("linkUrl") val linkUrl:String,
        @SerializedName("recId") val recId:Int,
        @SerializedName("sort") val sort:Int,
        @SerializedName("title") val title:String,
        @SerializedName("styleType") val styleType:Int,
        @SerializedName("coverPic") val coverPic:String,

        @SerializedName("place3") var place3: List<HomeRecomSubBean>,
        @SerializedName("place4") var place4: List<HomeRecomSubBean>,
        @SerializedName("isAdver") var isAdver:Boolean = false
        ) : MultiItemEntity{


    override fun getItemType(): Int {
        return type
    }

}