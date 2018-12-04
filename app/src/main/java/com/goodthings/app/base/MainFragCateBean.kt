package com.goodthings.app.base

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/26
 * 修改内容：
 * 最后修改时间：
 */
class MainFragCateBean(
        @SerializedName("cateId") val cateId:Int,
        @SerializedName("cateName") val cateName:String,
        @SerializedName("isDefault") val isDefault:Int,
        @SerializedName("prdCateId") val prdCateId:Int) {
}