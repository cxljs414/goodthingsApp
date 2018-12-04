package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/24
 * 修改内容：
 * 最后修改时间：
 */
class LogisBean(
        @SerializedName("name")val name:String,
        @SerializedName("no")val no:String
) {
}