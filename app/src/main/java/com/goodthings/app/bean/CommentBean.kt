package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/18
 * 修改内容：
 * 最后修改时间：
 */
data class CommentBean(
        @SerializedName("relate_id")val relate_id:Int,
        @SerializedName("content")val content:String,//内容
        @SerializedName("count")val count:Int,//回复数量
        @SerializedName("create_time")val create_time:String,//评论时间
        @SerializedName("fabulous")val fabulous:Int,//点赞数量
        @SerializedName("head_url")val head_url:String,//评论者头像
        @SerializedName("hf")val hf:String,//回复内容
        @SerializedName("hfName")val hfName:String,//回复者名字
        @SerializedName("mId")val mId:Int,//品论id
        @SerializedName("nickname")val nickname:String//评论者名字
) {

}