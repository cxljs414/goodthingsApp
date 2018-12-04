package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/8
 * 修改内容：
 * 最后修改时间：
 */


data class ShopBean(
		@SerializedName("msg") val msg: String,
		@SerializedName("data") val data: Data,
		@SerializedName("status") val status: String
) {
	data class Data(
			@SerializedName("collage") val collage: List<Collage>,
			@SerializedName("crowd") val crowd: List<ProdCrowdBean>,
			@SerializedName("count") val count: Count,
			@SerializedName("spu") val spu: List<Spu>
	) {
		data class Spu(
				@SerializedName("sumstock") val sumstock: Int,
				@SerializedName("createtime") val createtime: String,
				@SerializedName("maxprice") val maxprice: Double,
				@SerializedName("kcatename") val kcatename: String,
				@SerializedName("isfree") val isfree: Int,
				@SerializedName("kid") val kid: Int,
				@SerializedName("count") val count: Int,
				@SerializedName("remark") val remark: String,
				@SerializedName("pid") val pid: Int,
				@SerializedName("prokey") val prokey: Int,
				@SerializedName("type") val type: Int,
				@SerializedName("brandname") val brandname: String,
				@SerializedName("coverurl") val coverurl: String,
				@SerializedName("sale") val sale: Int,
				@SerializedName("prdtitle") val prdtitle: String,
				@SerializedName("minprice") val minprice: Double,
				@SerializedName("prdname") val prdname: String,
				@SerializedName("state") val state: Int,
				@SerializedName("did") val did: Int,
				@SerializedName("catename") val catename: String
		)
		data class Count(
				@SerializedName("update_time") val update_time: Long,
				@SerializedName("mer_id") val mer_id: Int,
				@SerializedName("total_prd_num") val total_prd_num: Int,
				@SerializedName("total_sale_num") val total_sale_num: Int
		)
		data class Collage(
				@SerializedName("cover_url") val cover_url: String,
				@SerializedName("mer_id") val mer_id: Int,
				@SerializedName("person_num") val person_num: Int,
				@SerializedName("create_time") val create_time: Long,
				@SerializedName("sale_num") val sale_num: Int,
				@SerializedName("old_price") val old_price: Double,
				@SerializedName("kucun") val kucun: Int,
				@SerializedName("title") val title: String,
				@SerializedName("stock_type") val stock_type: Int,
				@SerializedName("is_open_auto") val is_open_auto: Int,
				@SerializedName("price") val price: Double,
				@SerializedName("id") val id: Int,
				@SerializedName("shelve_status") val shelve_status: Int,
				@SerializedName("virtual_num") val virtual_num: Int,
				@SerializedName("spu_id") val spu_id: Int,
				@SerializedName("limit_times") val limit_times: Int
		)
		data class Crowd(
				@SerializedName("eid") val eid: Int,
				@SerializedName("show_plan_url") val show_plan_url: String,
				@SerializedName("cover_pic") val cover_pic: String,
				@SerializedName("finance_plan_url") val finance_plan_url: String,
				@SerializedName("cf_plan_url") val cf_plan_url: String,
				@SerializedName("title") val title: String,
				@SerializedName("type") val type: Int,
				@SerializedName("content") val content: String,
				@SerializedName("short_title") val short_title: String,
				@SerializedName("head_url") val head_url: String,
				@SerializedName("price") val price: Double,
				@SerializedName("intro") val intro: String,
				@SerializedName("nickname") val nickname: String,
				@SerializedName("shelve_status") val shelve_status: Int,
				@SerializedName("stock") val stock: Int,
				@SerializedName("sub_cover_pic") val sub_cover_pic: String,
				@SerializedName("target_amt") val target_amt: Double,
				@SerializedName("is_manual_finish") val is_manual_finish: Int,
				@SerializedName("is_manual_over") val is_manual_over: Int,
				@SerializedName("mer_id") val mer_id: Int,
				@SerializedName("create_time") val create_time: Long,
				@SerializedName("sale_num") val sale_num: Int,
				@SerializedName("oldPrice") val oldPrice: Double,
				@SerializedName("end_time") val end_time: Long,
				@SerializedName("province_value") val province_value: String,
				@SerializedName("pId") val pId: Int,
				@SerializedName("is_beyond_stock") val is_beyond_stock: Int,
				@SerializedName("stock_type") val stock_type: Int,
				@SerializedName("start_time") val start_time: Long,
				@SerializedName("userCount") val userCount: Int,
				@SerializedName("is_more_buy") val is_more_buy: Int,
				@SerializedName("sale_amt") val sale_amt: Double,
				@SerializedName("send_days") val send_days: Int,
				@SerializedName("limit_num") val limit_num: Int,
				@SerializedName("status") val status: Int
		)
	}
}
