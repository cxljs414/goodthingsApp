package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/19
 * 修改内容：
 * 最后修改时间：
 */
data class HotCityBean(
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String
        ) {
}