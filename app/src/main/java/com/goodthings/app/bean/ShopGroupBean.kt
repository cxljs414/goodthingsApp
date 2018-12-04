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


data class ShopGroupBean(
		@SerializedName("msg") val msg: String,
		@SerializedName("data") val data: List<Collage>,
		@SerializedName("status") val status: String
) {
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
}
