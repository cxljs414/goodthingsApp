package com.goodthings.app.http

import com.goodthings.app.bean.AfterSaleBean
import com.goodthings.app.bean.BaseResult
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/25
 * 修改内容：
 * 最后修改时间：
 */
interface ApiResService {

    //获取物流详情
    @POST("kdniaoTrack/unauth/query")
    @FormUrlEncoded
    fun queryLogisDetail(@Field("ShipperCode")ShipperCode:String,
                         @Field("billNo")billNo:String): Call<String>


    @POST("orderReturn/unauth/addOrderReturnApply")
    @FormUrlEncoded
    fun updateOrderReturnApply(@Field("type")type:Int,
                               @Field("orderNo")orderNo:String,
                               @Field("applyType")applyType:Int,
                               @Field("goodsState")goodsState:Int,
                               @Field("reason")reason:String,
                               @Field("returnAmt")returnAmt:String,
                               @Field("remark")remark:String,
                               @Field("phone")phone:String,
                               @Field("picUrl")picUrl:String ,
                               @Field("userId")userId:Int,
                               @Field("id")id:Int,
                               @Field("returnOrderNo")returnOrderNo:String): Call<String>


    @POST("orderReturn/unauth/addOrderReturnApply")
    @FormUrlEncoded
    fun addOrderReturnApply(@Field("type")type:Int,
                            @Field("orderNo")orderNo:String,
                            @Field("applyType")applyType:Int,
                            @Field("goodsState")goodsState:Int,
                            @Field("reason")reason:String,
                            @Field("returnAmt")returnAmt:String,
                            @Field("remark")remark:String,
                            @Field("phone")phone:String,
                            @Field("picUrl")picUrl:String ,
                            @Field("userId")userId:Int ):Call<String>

}