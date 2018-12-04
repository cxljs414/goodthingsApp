package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/16
 * 修改内容：
 * 最后修改时间：
 */
class HomeNewsBean(@SerializedName("cate_name") val cate_name: String,
                   @SerializedName("id") val id: String,
                   @SerializedName("title") val title: String) {
}