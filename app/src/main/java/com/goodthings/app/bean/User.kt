package com.goodthings.app.bean
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/2/7
 * 修改内容：
 * 最后修改时间：
 */

data class User(
		@SerializedName("age_range") var age_range: Int,
		@SerializedName("id") val id: Int,
		@SerializedName("umeng_alias") val umeng_alias: String,
		@SerializedName("umeng_alias_type") val umeng_alias_type: String,
		@SerializedName("user_id") val user_id: String,
		@SerializedName("nickname") var nickname: String,
		@SerializedName("eng_name") val eng_name: String,
		@SerializedName("password") val password: String,
		@SerializedName("head_url") var head_url: String,
		@SerializedName("birth_date") val birth_date: String,
		@SerializedName("sex_key") var sex_key: Int,//男：2   女：3
		@SerializedName("sex_value") var sex_value: String,
		@SerializedName("wechat") val wechat: String,
		@SerializedName("phone") val phone: String,
		@SerializedName("longitude") val longitude: String,
		@SerializedName("dimensionality") val dimensionality: String,
		@SerializedName("liveness") val liveness: String,
		@SerializedName("liveness_number") val liveness_number: String,
		@SerializedName("abstract_self") val abstract_self: String,
		@SerializedName("abstract_business") val abstract_business: String,
		@SerializedName("company_logoUrl") val company_logoUrl: String,
		@SerializedName("company_name") val company_name: String,
		@SerializedName("push") val push: String,
		@SerializedName("state") val state: Int,
		@SerializedName("wx_openid") val wx_openid: String,
		@SerializedName("province_key") val province_key: String,
		@SerializedName("province_value") val province_value: String,
		@SerializedName("auth_token") val auth_token: String,
		@SerializedName("ios_device_token") val ios_device_token: String,
		@SerializedName("android_device_token") val android_device_token: String,
		@SerializedName("device_type") val device_type: String,
		@SerializedName("remarks") val remarks: String,
		@SerializedName("title") val title: String,
		@SerializedName("company_details_name") val company_details_name: String,
		@SerializedName("company_address") val company_address: String,
		@SerializedName("reg_from") val reg_from: String,
		@SerializedName("create_time") val create_time: String,
		@SerializedName("update_time") val update_time: String
) : Parcelable {
	constructor(source: Parcel) : this(
			source.readInt(),
			source.readInt(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readInt(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readInt(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString(),
			source.readString()
	)

	override fun describeContents() = 0

	override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
		writeInt(age_range)
		writeInt(id)
		writeString(umeng_alias)
		writeString(umeng_alias_type)
		writeString(user_id)
		writeString(nickname)
		writeString(eng_name)
		writeString(password)
		writeString(head_url)
		writeString(birth_date)
		writeInt(sex_key)
		writeString(sex_value)
		writeString(wechat)
		writeString(phone)
		writeString(longitude)
		writeString(dimensionality)
		writeString(liveness)
		writeString(liveness_number)
		writeString(abstract_self)
		writeString(abstract_business)
		writeString(company_logoUrl)
		writeString(company_name)
		writeString(push)
		writeInt(state)
		writeString(wx_openid)
		writeString(province_key)
		writeString(province_value)
		writeString(auth_token)
		writeString(ios_device_token)
		writeString(android_device_token)
		writeString(device_type)
		writeString(remarks)
		writeString(title)
		writeString(company_details_name)
		writeString(company_address)
		writeString(reg_from)
		writeString(create_time)
		writeString(update_time)
	}

	companion object {
		@JvmField
		val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
			override fun createFromParcel(source: Parcel): User = User(source)
			override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
		}
	}
}