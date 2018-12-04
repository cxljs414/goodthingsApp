package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/8
 * 修改内容：
 * 最后修改时间：
 */
data class CrowdOrderBean(
        @SerializedName("id") val id: Int,
        @SerializedName("fee") val fee: Double,
        @SerializedName("order_no") val order_no: String,
        @SerializedName("status") var status: Int,//1未支付 2已取消3已支付 4已发货 5已完成/未评价 6已评价 7已申请退款 8已退款 9退款拒绝',
        @SerializedName("trans_amt") val trans_amt: Double,
        @SerializedName("price") val price: Double,
        @SerializedName("count") val count: Int,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("head_url") val head_url: String,
        @SerializedName("short_title") val short_title: String,
        @SerializedName("title") val title: String,
        @SerializedName("cover_pic") val cover_pic: String,
        @SerializedName("stock") val stock: Int,
        @SerializedName("sale_num") val sale_num: Int,
        @SerializedName("sale_amt") val sale_amt: Double,
        @SerializedName("eId") val eId: Int,
        @SerializedName("pId") val pId: Int,
        @SerializedName("intro") val intro: String,
        @SerializedName("name") val name: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("addr") val addr: String,
        @SerializedName("create_time") val create_time: String,
        @SerializedName("finish_time") val finish_time: String,
        @SerializedName("end_time") val end_time: String,//商品众筹截止时间
        @SerializedName("is_manual_finish") val is_manual_finish: Int,//是否手动成功
        @SerializedName("pStatus") val pStatus: Int,//状态0进行中1成功完成2失败结束
        @SerializedName("logis_company") var logis_company: String = "",
        @SerializedName("way_bill_no") var way_bill_no: String = "",
        @SerializedName("sub_cover_pic") val sub_cover_pic: String
) {
}