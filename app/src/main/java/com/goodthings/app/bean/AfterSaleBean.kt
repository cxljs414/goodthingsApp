package com.goodthings.app.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/23
 * 修改内容：
 * 最后修改时间：
 */
class AfterSaleBean(
        @SerializedName("nickname") val nickname: String,
        @SerializedName("applyId") val applyId: Int,
        @SerializedName("user_id") val user_id: Int,
        @SerializedName("type") val type: Int,//1拼团
        @SerializedName("order_no") val order_no: String,
        @SerializedName("return_order_no") val return_order_no: String,
        @SerializedName("apply_type") val apply_type: Int, //退款类型0我要退款退货1我要退款（无需退货）
        @SerializedName("goods_state") val goods_state: Int,
        @SerializedName("reason") val reason: String,
        @SerializedName("return_amt") val return_amt: Double,
        @SerializedName("remark") val remark: String,
        @SerializedName("phone") val phone: String,
        @SerializedName("pic_url") val pic_url: String,
        @SerializedName("create_time") val create_time: String,
        @SerializedName("logis_company") val logis_company: String,
        @SerializedName("logis_no") val logis_no: String,
        @SerializedName("way_bill_no") val way_bill_no: String,
        @SerializedName("mstatus") val mstatus: Int,
        @SerializedName("moldorderno") val moldorderno: String,
        @SerializedName("coldorderno") val coldorderno: String,
        @SerializedName("crefund_fail_reason") val crefund_fail_reason: String,//驳回原因
        @SerializedName("cstatus") var cstatus: Int//0退款初始状态1退款中 2退款成功3退款失败4退款拒绝5退款取消6退款已审核7退款中（拼团成功后的）
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readDouble(),
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
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(nickname)
        writeInt(applyId)
        writeInt(user_id)
        writeInt(type)
        writeString(order_no)
        writeString(return_order_no)
        writeInt(apply_type)
        writeInt(goods_state)
        writeString(reason)
        writeDouble(return_amt)
        writeString(remark)
        writeString(phone)
        writeString(pic_url)
        writeString(create_time)
        writeString(logis_company)
        writeString(logis_no)
        writeString(way_bill_no)
        writeInt(mstatus)
        writeString(moldorderno)
        writeString(coldorderno)
        writeString(crefund_fail_reason)
        writeInt(cstatus)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<AfterSaleBean> = object : Parcelable.Creator<AfterSaleBean> {
            override fun createFromParcel(source: Parcel): AfterSaleBean = AfterSaleBean(source)
            override fun newArray(size: Int): Array<AfterSaleBean?> = arrayOfNulls(size)
        }
    }
}