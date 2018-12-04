package com.goodthings.app.bean

import com.google.gson.annotations.SerializedName

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/6/5
 * 修改内容：
 * 最后修改时间：
 */
data class ProdBuyDetailBean(
        @SerializedName("id")val id:Int,
        @SerializedName("cateName")val cateName:String,
        @SerializedName("detailStr")val detailStr:String,
        var subDetailList:List<ProdBuyDetailSubBean>
) {
}

class ProdBuyDetailSubBean(
        var skuDetailId:Int,
        var standard:String,
        var price:String,
        var stock:String,
        var cover_url:String
){

}