package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/4/25
 * 修改内容：
 * 最后修改时间：
 */
data class ProdCrowdBean(
        @SerializedName("head_url") val head_url: String,
        @SerializedName("nickname") val nickname: String,
        @SerializedName("province_value") val province_value: String,
        @SerializedName("mer_id") val mer_id: Int,
        @SerializedName("pId") val pId: Int,
        @SerializedName("short_title") val short_title: String,
        @SerializedName("title") val title: String,
        @SerializedName("intro") val intro: String,
        @SerializedName("cover_pic") val cover_pic: String,
        @SerializedName("price") val price: Double,
        @SerializedName("oldPrice") val oldPrice: Double,
        @SerializedName("target_amt") val target_amt: String,
        @SerializedName("content") val content: String,
        @SerializedName("cf_plan_url") val cf_plan_url: String,
        @SerializedName("finance_plan_url") val finance_plan_url: String,
        @SerializedName("show_plan_url") val show_plan_url: String,
        @SerializedName("start_time") val start_time: String,
        @SerializedName("end_time") val end_time: String,
        @SerializedName("limit_num") val limit_num: Int,
        @SerializedName("stock_type") val stock_type: Int,
        @SerializedName("is_beyond_stock") val is_beyond_stock: Int,//是否允许超出库存购买0否1是
        @SerializedName("is_more_buy") val is_more_buy: Int,//是否允许多次购买0否1是
        @SerializedName("is_manual_finish") val is_manual_finish: Int,
        @SerializedName("is_manual_over") val is_manual_over: Int,
        @SerializedName("shelve_status") val shelve_status: Int,
        @SerializedName("send_days") val send_days: Int,
        @SerializedName("status") val status: Int,//状态0进行中1成功完成2失败结束
        @SerializedName("oper_id") val oper_id: Int,
        @SerializedName("create_time") val create_time: String,
        @SerializedName("type") val type: Int,
        @SerializedName("eid") val eid: Int,
        @SerializedName("stock") val stock: Int,
        @SerializedName("sale_num") val sale_num: Int,
        @SerializedName("sale_num_tmp") val sale_num_tmp: Int,
        @SerializedName("userCount") val userCount: Int,
        @SerializedName("sale_amt") val sale_amt: Double,
        @SerializedName("sub_cover_pic") val sub_cover_pic: String,
        @SerializedName("covers") val covers: List<String>
)  {
}
