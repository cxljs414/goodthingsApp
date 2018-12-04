package com.goodthings.app.bean
import com.google.gson.annotations.SerializedName


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/9
 * 修改内容：
 * 最后修改时间：
 */

data class HomeGussLikeBean(
		@SerializedName("imgUrl") val imgUrl: String,
		@SerializedName("place_type") val place_type: Int,
		@SerializedName("type") val type: Int,
		@SerializedName("id") val id: Int,
		@SerializedName("mer_id") val mer_id: Int,
		@SerializedName("prd_name") val prd_name: String,
		@SerializedName("prd_title") val prd_title: String,
		@SerializedName("cover_url") val cover_url: String,
		@SerializedName("state") val state: Int,
		@SerializedName("collectCount") val collectCount: Int,
		@SerializedName("viewCount") val viewCount: Int,
		@SerializedName("head_url") val head_url: String,
		@SerializedName("nickname") val nickname: String,
		@SerializedName("spuId") val spuId: String
		)