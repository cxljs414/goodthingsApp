package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/5
 * 修改内容：
 * 最后修改时间：
 */
data class GroupOrderDetailBean(
        @SerializedName("id") val id: Int,
        @SerializedName("tId") val tId: Int,
        @SerializedName("order_no") val order_no: String,
        @SerializedName("create_time") var create_time: String,
        @SerializedName("name") val name: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("pay_type") val pay_type: Double,
        //0未支付1已取消2已支付/待分享3待发货 4已发货 5已完成/未评价 6已评价 7退款中8已退款 9退款失败
        @SerializedName("status") var status: Int,
        @SerializedName("title") val title: String,
        @SerializedName("trans_amt") val trans_amt: Double,
        @SerializedName("logis_company") var logis_company: String,
        @SerializedName("logis_no") var logis_no: String,
        @SerializedName("way_bill_no") var way_bill_no: String,
        @SerializedName("addr") var addr: String,
        @SerializedName("pid") val pid: Int,
        @SerializedName("buy_num") val buy_num: Int,
        @SerializedName("sku_detail_id") val sku_detail_id: Int,
        @SerializedName("head_url") val head_url: List<String>,
        @SerializedName("price") val price: Double,
        @SerializedName("cover_url") val cover_url: String,
        @SerializedName("person_num")val person_num:Int,
        @SerializedName("is_shou")val is_shou:Int //0 未申请 1申请过
) {
}