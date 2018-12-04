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


data class ShopCountBean(
		@SerializedName("msg") val msg: String,
		@SerializedName("data") val data: Count,
		@SerializedName("status") val status: String
) {
	data class Count(
			@SerializedName("update_time") val update_time: Long,
			@SerializedName("mer_id") val mer_id: Int,
			@SerializedName("total_prd_num") val total_prd_num: Int,
			@SerializedName("total_sale_num") val total_sale_num: Int
	)
}
