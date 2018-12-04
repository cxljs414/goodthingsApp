package com.goodthings.app.bean
import com.google.gson.annotations.SerializedName


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/24
 * 修改内容：
 * 最后修改时间：
 */

data class ExpressDetailBean(
		@SerializedName("LogisticCode") val logisticCode: String,
		@SerializedName("ShipperCode") val shipperCode: String,
		@SerializedName("Traces") val traces: List<Trace>,
		@SerializedName("State") val state: String,
		@SerializedName("EBusinessID") val eBusinessID: String,
		@SerializedName("Success") val success: Boolean,
		@SerializedName("coverPic") var coverPic: String,
		@SerializedName("title") var title: String,
		@SerializedName("logisCompany") var logisCompany: String,
		@SerializedName("loggisNo") var loggisNo: String
) {
	data class Trace(
			@SerializedName("AcceptStation") val acceptStation: String,
			@SerializedName("AcceptTime") val acceptTime: String
	)
}