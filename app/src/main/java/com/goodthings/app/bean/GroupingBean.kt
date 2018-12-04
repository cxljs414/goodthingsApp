package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/4
 * 修改内容：
 * 最后修改时间：
 */
data class GroupingBean(
        @SerializedName("tId") val tId: Int,
        @SerializedName("user_id") val user_id: Int,
        @SerializedName("col_id") val col_id: Int,
        @SerializedName("person_num") val person_num: Int,
        //@SerializedName("status") val status: Int,//团状态0未付款1拼团中2拼团成功3拼团失败
        @SerializedName("create_time") val create_time: String,
        @SerializedName("is_finish") val is_finish: Int,//是否虚拟完成0否1是
        @SerializedName("head_url") val head_url: String,
        @SerializedName("nickname") val nickname: String
) {
}