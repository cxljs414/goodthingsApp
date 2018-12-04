package com.goodthings.app.bean
import com.google.gson.annotations.SerializedName


/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/7
 * 修改内容：
 * 最后修改时间：
 */


data class HomeRecomBean(
		@SerializedName("place1") val place1: List<HomeRecomSubBean>,//banner
		@SerializedName("place2") val place2: List<HomeRecomSubBean>,//四个分类
		@SerializedName("place3") val place3: List<HomeRecomSubBean>,//专题
		@SerializedName("place4") val place4: List<HomeRecomSubBean>,//他们在这而
		@SerializedName("place5") val place5: List<HomeRecomSubBean>,//首页新闻推荐
		@SerializedName("place6") val place6: List<HomeRecomSubBean>,//首页预购推荐
		@SerializedName("place7") val place7: List<HomeRecomSubBean>,//首页领袖ip推荐
		@SerializedName("place8") val place8: List<HomeRecomSubBean>, //首页行业ip推荐
		@SerializedName("place9") val place9: List<HomeRecomSubBean> //首页拼团推荐
) {
}