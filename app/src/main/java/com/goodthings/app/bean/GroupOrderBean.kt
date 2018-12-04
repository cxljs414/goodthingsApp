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
class GroupOrderBean(@SerializedName("cover_url") val cover_url: String,
                     @SerializedName("title") val title: String,
                    //0未支付1已取消2已支付/待分享3待发货 4已发货 5已完成/未评价 6已评价 7退款中8已退款 9退款失败
                     @SerializedName("status") var status: Int,
                     @SerializedName("create_time") val create_time: String,
                     @SerializedName("pro_value") val pro_value: String,
                     @SerializedName("trans_amt") val trans_amt: Double,
                     @SerializedName("buy_num") val buy_num: Int,
                     @SerializedName("order_no") val order_no: String,
                     @SerializedName("oId") val oId: Int,
                     @SerializedName("tId") val tId: Int,
                     @SerializedName("col_id")val col_id:Int) {

}