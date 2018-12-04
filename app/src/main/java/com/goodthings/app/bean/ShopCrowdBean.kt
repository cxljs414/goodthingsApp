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


data class ShopCrowdBean(
		@SerializedName("msg") val msg: String,
		@SerializedName("data") val data: List<ProdCrowdBean>,
		@SerializedName("status") val status: String
) {
}
