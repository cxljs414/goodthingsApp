package com.goodthings.app.bean

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/19
 * 修改内容：
 * 最后修改时间：
 */
data class CityBean (
        @SerializedName("id") val id: String,
        @SerializedName("name") val name: String,
        @SerializedName("parent_id") val parent_id: String,
        @SerializedName("spell") val spell: String,
        @SerializedName("word") val word: String,
        @SerializedName("type") var type: Int,
        @SerializedName("hotcitys") var hotcitys: List<HotCityBean>?

) : MultiItemEntity{
    override fun getItemType(): Int {
        return type
    }

}