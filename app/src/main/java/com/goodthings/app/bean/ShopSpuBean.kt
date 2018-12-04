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


data class ShopSpuBean(
		@SerializedName("msg") val msg: String,
		@SerializedName("data") val data: List<Spu>,
		@SerializedName("status") val status: String
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
}
