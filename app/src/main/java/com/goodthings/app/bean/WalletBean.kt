package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/9
 * 修改内容：
 * 最后修改时间：
 */
data class WalletBean(
        @SerializedName("coinBalance") val coinBalance:Double,//金币账户
        @SerializedName("coinChangeBalance") val coinChangeBalance:Double,//我的金币
        @SerializedName("balance") val balance:Double,//账户余额
        @SerializedName("balanceH") val balanceH:Double,//昨日1
        @SerializedName("balanceL") val balanceL:Double,////昨日汇率
        @SerializedName("balanceC") val balanceC:Double) {//昨日2
}