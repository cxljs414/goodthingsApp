package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/4
 * 修改内容：
 * 最后修改时间：
 */
data class GoldBean(
        @SerializedName("type") val type: Int,
        @SerializedName("coin") val coin: Int,
        @SerializedName("coin_balance") val coin_balance: Float,
        @SerializedName("chanel") val chanel: String,
        @SerializedName("create_time") val create_time: String,
        @SerializedName("coin_change") val coin_change: String,
        @SerializedName("coin_change_balance") val coin_change_balance: String
        ) {
}