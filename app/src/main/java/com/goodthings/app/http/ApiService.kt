package com.goodthings.app.http

import com.goodthings.app.base.MainFragCateBean
import com.goodthings.app.bean.*
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
interface ApiService {

    @GET("facade/unauth/appVersions") fun checkVersion():Flowable<BaseResult<UpdateAppBean>>
    @GET("facade/unauth/bigAdvertising") fun requestGuide():Flowable<BaseResult<GuideBean>>
    @Streaming
    @GET
    fun download(@Url url: String): Flowable<ResponseBody>

    //登录
    @GET("sysUser/unauth/otoregist")
    fun queryUserByPhone(@Query("phone") phone:String):Flowable<BaseResult<User>>

    //原生登录与web同步
    @GET("sysUser/unauth/setUserCache")
    fun loginUserCache(@Query("userId") userId:Int):Flowable<CommonResult>

    //发送验证码
    @GET("sysUser/unauth/sendMsg")
    fun sendVerifCode(@Query("phone") phone: String):Flowable<BaseResult<String>>

    //手机号密码登录
    @POST("sysUser/unauth/louspw")
    @FormUrlEncoded
    fun loginbyPassword(@Field("phone") phone: String,
                        @Field("password") psd:String
    ):Flowable<CommonResult>

    //修改密码
    @POST("sysUser/unauth/uppwd")
    @FormUrlEncoded
    fun updatePassword(@Field("phone") phone: String,
                       @Field("password") password:String
    ):Flowable<CommonResult>

    //上传图片
    @POST("sysUser/unauth/uploadimage")
    @FormUrlEncoded
    fun uploadimage(@Field("image") imgString: String): Flowable<BaseResult<String>>

    //保存个人信息
    @POST("sysUser/unauth/adus")
    @FormUrlEncoded
    fun registPsersonInfo(@Field("image") headurl: String,
                          @Field("nickName") nickname: String,
                          @Field("sex") sexKey: String?,
                          @Field("age") age: String?,
                          @Field("key") key: String?,
                          @Field("phone") phone: String?,
                          @Field("pwd") pwd: String?
    ): Flowable<BaseResult<RegistUserBean>>

    //第一次注册
    @POST("sysUser/unauth/topersion")
    @FormUrlEncoded
    fun firstRegist(@Field("phone")phone: String,
                    @Field("password")password: String
    ): Flowable<CommonResult>

    //猜你喜欢
    @GET("facade/unauth/getLike")
    fun guessYouLike():Flowable<BaseResult<List<HomeGussLikeBean>>>

    //猜你喜欢 登录后
    @GET("facade/unauth/getLike")
    fun guessYouLikeByUserId(@Query("userId") userId: Int):Flowable<BaseResult<List<HomeGussLikeBean>>>

    //首页内容
    @POST("facade/unauth/getRecContentList")
    @FormUrlEncoded
    fun getRecContentList(@Field("pageId") pageId: Int,
                          @Field("curDate") curDate: String,
                          @Field("cityCode") cityCode: String,
                          @Field("userId") userId: Int
    ):Flowable<BaseResult<HomeRecomBean>>

    //好事头条
    @GET("facade/unauth/getTopLineList")
    fun getNewsList():Flowable<BaseResult<List<HomeNewsBean>>>

    //是否有新的消息（通过获取总数量和本机保存消息数量对比）
    @GET("sysUser/unauth/getMsgCount")
    fun getMsgCount(): Flowable<BaseResult<Int>>

    //通过城市名来获取城市code
    @POST("facade/unauth/getCode")
    @FormUrlEncoded
    fun getCityCode(@Field("city") city:String): Flowable<BaseResult<String>>

    //获取分类
    @POST("mallPrt/unauth/getCateList")
    @FormUrlEncoded
    fun getCateList(@Field("cityCode") cityCode: String): Flowable<BaseResult<List<MainFragCateBean>>>

    //分类下商品列表接口
    @POST("mallPrt/unauth/getPrdList")
    @FormUrlEncoded
    fun getPrdList(@Field("cateId") cateId:Int,
                   @Field("cityCode") cityCode:String,
                   @Field("startIndex") startIndex:Int,
                   @Field("pageSize") pageSize:Int
    ):Flowable<BaseResult<PageBean<MainFragProdBean>>>

    //非固定推荐内容列表接口
    @POST("facade/unauth/getNotFixContentList")
    @FormUrlEncoded
    fun getNotFixContentList(@Field("pageId") pageId:Int,
                             @Field("cateId") cateId:Int,
                             @Field("cityCode") cityCode:String,
                             @Field("curDate") curDate:String
    ):Flowable<BaseResult<List<MainFragProdBean>>>

    @POST("facade/unauth/tyDailyRedEnvelopes")
    @FormUrlEncoded
    fun tyDailyRedEnvelopes(@Field("userId") userId: Int): Flowable<CommonResult>

    @POST("facade/unauth/dailyRedEnvelopes")
    @FormUrlEncoded
    fun dailyRedEnvelopes(@Field("userId") userId: Int): Flowable<CommonResult>

    @POST("user/unauth/getMyWallet")
    @FormUrlEncoded
    fun getMyWallet(@Field("userId")userId: Int):Flowable<BaseResult<WalletBean>>

    @POST("user/unauth/queryCoinRecord")
    @FormUrlEncoded
    fun queryCoinRecord(@Field("userId") userId: Int,
                        @Field("page") page: Int,
                        @Field("rows") rows: Int):Flowable<PageBean<GoldBean>>

    @POST("user/unauth/queryCoinSeleRecord")
    @FormUrlEncoded
    fun queryCoinSeleRecord(@Field("userId")userId: Int,
                            @Field("page")page: Int,
                            @Field("rows")rows: Int):Flowable<PageBean<GoldBean>>


    @POST("issue/unauth/registShareGoldEnvelopes")
    @FormUrlEncoded
    fun registShareGoldEnvelopes(@Field("userId")userId: Int):Flowable<BaseResult<Int>>

    @POST("issue/unauth/UserShareGoldEnvelopes")
    @FormUrlEncoded
    fun userShareGoldEnvelopes(@Field("userId")userId:Int):Flowable<CommonResult>

    @POST("issue/unauth/ShareGoldEnvelopes")
    @FormUrlEncoded
    fun shareGoldEnvelopes(
            @Field("userId")userId:Int,
            @Field("cententId")cententId:Int,
            @Field("type")type:Int):Flowable<CommonResult>

    @POST("/user/unauth/share")
    @FormUrlEncoded
    fun share(
            @Field("userId")userId:Int,
            @Field("relate_id")relate_id:Int,
            @Field("type")type:String):Flowable<CommonResult>

    /**
     * 获取所有城市
     */
    @GET("facade/unauth/citys")
    fun getCitys():Flowable<BaseResult<List<CityBean>>>

    /**
     * 获取二级城市
     */
    @GET("facade/unauth/cityson")
    fun cityson(@Query("id")id:Int):Flowable<BaseResult<List<SysCityArea>>>

    @POST("crowdinfo/unauth/crowdQuery")
    @FormUrlEncoded
    fun crowdQuery(
            @Field("page")page: Int,
            @Field("rows")rows: Int
    ):Flowable<PageBean<ProdCrowdBean>>

    @POST("crowdinfo/unauth/crowdQueryByid")
    @FormUrlEncoded
    fun crowdDetail(@Field("crowdId")crowdId: Int):Flowable<BaseResult<ProdCrowdBean>>

    @GET("app/user/addfollow")
    fun addfollow(
            @Query("userId") userId: Int,
            @Query("follow_user_id") follow_user_id: Int): Flowable<CommonResult>

    @GET("app/user/delonefollow")
    fun delonefollow(
            @Query("userId") userId: Int,
            @Query("follow_user_id") follow_user_id: Int): Flowable<CommonResult>

    @POST("app/issue/querySkillUser")
    @FormUrlEncoded
    fun querySkillUser(
            @Field("userId")userId:Int,
            @Field("follow_userId")follow_userId:Int):Flowable<BaseResult<ScrowdUserBean>>

    @POST("app/user/getallmsg")
    @FormUrlEncoded
    fun getallmsg(@Field("userId")userId:Int,
                  @Field("skill_id")skill_id:Int,
                  @Field("type")type:Int,
                  @Field("relate_id")relate_id:Int,
                  @Field("follow_user_id")follow_user_id:Int):Flowable<BaseResult<FollowBean>>

    @GET("user/unauth/collectCount")
    fun collectCount(@Query("type")type:Int,
                     @Query("relate_id")relate_id:Int):Flowable<BaseResult<Int>>


    @POST("user/unauth/shareCount")
    @FormUrlEncoded
    fun shareCount(@Field("type")type:Int,
                   @Field("relate_id")relate_id:Int):Flowable<BaseResult<Int>>


    @GET("app/user/addcollect")
    fun addcollect(
            @Query("userId") userId: Int,
            @Query("type") type: Int,
            @Query("relate_id") relate_id: Int): Flowable<CommonResult>

    @GET("app/user/delonecollect")
    fun delonecollect(
            @Query("userId") userId: Int,
            @Query("type") type: Int,
            @Query("relate_id") relate_id: Int): Flowable<CommonResult>

    /**
     * 请求用户默认地址
     */
    @GET("app/user/queryDefaultAddr")
    fun queryDefaultAddr(@Query("userId")type:Int):Flowable<BaseResult<AddressBean>>

    /**
     * 生成订单
     */
    @Headers("User-Agent:Android")
    @POST("cfOrder/unauth/addOrder")
    @FormUrlEncoded
    fun addOrder(@Field("type")type:Int,
                 @Field("transAmt")transAmt:Double,
                 @Field("fee")fee:Double,
                 @Field("cfId")cfId:Int,
                 @Field("cfExtId")cfExtId:Int,
                 @Field("buyNum")buyNum:Int,
                 @Field("addrId")addrId:Int,
                 @Field("userId")userId:Int):Flowable<CommonResult>

    /**
     * 获取微信支付信息
     */
    @GET("app/unauth/getWxPayInit")
    fun getWxPayInit(
            @Query("userId")userId:Int,
            @Query("orderNum")orderNum:String,
            @Query("type")type:Int):Flowable<BaseResult<WxPayInitBean>>


    /**
     * 获取微信支付信息
     */
    @Headers("User-Agent:Android")
    @GET("wechat/getWxPayInit")
    fun getWxPayInit(
            @Query("orderNum")orderNum:String,
            @Query("type")type:Int):Flowable<BaseResult<WxPayInitBean>>

    /**
     * 新增收货地址
     */
    @POST("app/user/unauth/addAddress")
    @FormUrlEncoded
    fun addAddress(@Field("user_id")user_id:Int,
                   @Field("name")name:String,
                   @Field("phone")phone:String,
                   @Field("city")city:String,
                   @Field("addr")addr:String,
                   @Field("city_code")city_code:String,
                   @Field("is_default")is_default:Int//1是2否
    ):Flowable<CommonResult>

    /**
     * 已经参与预购的次数
     */
    @POST("crowdinfo/unauth/payQueryByid")
    @FormUrlEncoded
    fun payQueryByid(@Field("userId")userId:Int,
                     @Field("crowdId")crowdId:Int):Flowable<BaseResult<Int>>


    @POST("crowdinfo/unauth/shareCrowdRedEnvelopes ")
    @FormUrlEncoded
    fun shareCrowdRedEnvelopes(@Field("userId")userId:Int):Flowable<CommonResult>

    @POST("crowdinfo/unauth/ShareCrowdEnvelopes ")
    @FormUrlEncoded
    fun shareCrowdEnvelopes(@Field("userId")userId: Int): Flowable<BaseResult<Int>>

    //预购列表
    @POST("app/unauth/getCrowdOrderlist")
    @FormUrlEncoded
    fun getCrowdOrderlist(@Field("userId")userId: Int,
                          @Field("rows")rows: Int,
                          @Field("page")page: Int,
                          @Field("status")status: Int):Flowable<PageBean<CrowdOrderBean>>

    @POST("app/unauth/qxCfOrder")
    @FormUrlEncoded
    fun qxCfOrder(@Field("order_no")order_no: String,
                  @Field("count")count: Int,
                  @Field("eid")eid: Int,
                  @Field("status")status: Int):Flowable<CommonResult>


    @POST("app/unauth/getCfOrderByOrderNo")
    @FormUrlEncoded
    fun getCfOrderByOrderNo(@Field("orderNo")orderNo: String):Flowable<BaseResult<CrowdOrderBean>>

    @POST("crowdinfo/unauth/cfDailyGoldEnvelopes")
    @FormUrlEncoded
    fun cfDailyGoldEnvelopes(@Field("userId")userId: Int,
                             @Field("cententId")cententId: Int):Flowable<BaseResult<Int>>

    /**
     * 增加浏览记录
     */
    @POST("app/unauth/addBrowseLog")
    @FormUrlEncoded
    fun addBrowseLog(@Field("userId")userId: Int,
                     @Field("type")type: Int,
                     @Field("issueId")crowdId: Int):Flowable<CommonResult>

    /**
     * 地址列表
     */
    @GET("app/unauth/queryMyAddress")
    fun queryMyAddress(@Query("userId")userId:Int):Flowable<BaseResult<List<AddressBean>>>

    /**
     * 根据id获取地址信息
     */
    @GET("app/unauth/getAddress")
    fun getAddress(@Query("id")addrId: Int):Flowable<BaseResult<AddressBean>>

    /**
     * 更新地址
     */
    @POST("app/unauth/updateAddress")
    @FormUrlEncoded
    fun updateAddress(
            @Field("id")addrId: Int,
            @Field("user_id")user_id:Int,
            @Field("name")name:String,
            @Field("phone")phone:String,
            @Field("city")city:String,
            @Field("addr")addr:String,
            @Field("city_code")city_code:String,
            @Field("is_default")is_default:Int
    ):Flowable<CommonResult>


    /**
     * 拼团列表
     */
    @POST("collage/unauth/collageQuery")
    @FormUrlEncoded
    fun collageQuery( @Field("page")page: Int,
                      @Field("rows")rows:Int):Flowable<PageBean<GroupListBean>>

    /**
     * 交钱后推荐接口
     */
    @POST("collage/unauth/tjCollageQuery")
    @FormUrlEncoded
    fun tjCollageQuery(@Field("page")page: Int,
                       @Field("rows")rows:Int):Flowable<List<GroupListBean>>

    /**
     * 平团商品详情
     */
    @POST("collage/unauth/getCollageDetail")
    @FormUrlEncoded
    fun getCollageDetail(@Field("collageId")collageId:Int):Flowable<BaseResult<GroupBuyDetailBean>>

    /**
     * 当前可参与的拼团列表
     */
    @POST("collage/unauth/colTeamQuery")
    @FormUrlEncoded
    fun colTeamQuery(@Field("collageId")collageId:Int):Flowable<List<GroupingBean>>

    /**
     * 拼团生成订单
     */
    @Headers("User-Agent:Android")
    @POST("colOrder/unauth/addOrder")
    @FormUrlEncoded
    fun addGroupOrderNomal(@Field("transAmt")transAmt:Double,
                           @Field("fee")fee:Double,
                           @Field("colId")colId:Int,
                           @Field("skuDetailId")skuDetailId:Int,
                           @Field("buyNum")buyNum:Int,
                           @Field("addrId")addrId:Int,
                           @Field("userId")userId:Int):Flowable<CommonResult>

    /**
     * 拼团生成订单
     */
    @Headers("User-Agent:Android")
    @POST("colOrder/unauth/addOrder")
    @FormUrlEncoded
    fun addGroupOrderTeam(@Field("transAmt")transAmt:Double,
                          @Field("fee")fee:Double,
                          @Field("colId")colId:Int,
                          @Field("skuDetailId")skuDetailId:Int,
                          @Field("buyNum")buyNum:Int,
                          @Field("addrId")addrId:Int,
                          @Field("userId")userId:Int,
                          @Field("teamId")teamId:Int):Flowable<CommonResult>

    /**
     * 拼团订单列表
     */
    @POST("app/unauth/getCollageOrderlist")
    @FormUrlEncoded
    fun myCollageOrderlist(@Field("userId") userId: Int,
                           @Field("page") page: Int,
                           @Field("rows") rows: Int,
                           @Field("status")status: Int):Flowable<PageBean<GroupOrderBean>>

    /**
     * 取消拼团订单
     */
    @POST("app/unauth/qxCollageOrder")
    @FormUrlEncoded
    fun qxCollageOrder(
            @Field("old_status")old_status:Int,
            @Field("orderId")orderId:Int
    ):Flowable<CommonResult>

    /**
     * 拼团订单详情
     */
    @POST("app/unauth/gerCollageOrderDetail")
    @FormUrlEncoded
    fun gerCollageOrderDetail(
            @Field("oId")oId:Int
    ):Flowable<BaseResult<GroupOrderDetailBean>>

    /**
     * 拼团订单详情
     */
    @POST("app/unauth/getCollageOrderDetailByOrderNo")
    @FormUrlEncoded
    fun getCollageOrderDetailByOrderNo(
            @Field("orderNo")orderNo:String
    ):Flowable<BaseResult<GroupOrderDetailBean>>


    /**
     *是否有进行中拼团
     */
    @POST("colOrder/unauth/isBuy")
    @FormUrlEncoded
    fun isBuy(
            @Field("userId")userId:Int,
            @Field("cId")cId:Int
    ):Flowable<BaseResult<Boolean>>

    /**
     * 购买时的商品详情
     */
    @GET("app/getPrdDetail")
    fun getPrdDetail(@Query("id")id:Int):Flowable<List<ProdBuyDetailBean>>

    /**
     * 更新个人信息
     */
    @POST("sysUser/unauth/updateUserInfo")
    @FormUrlEncoded
    fun updateUserInfo(
            @Field("userId")userId: Int,
            @Field("sex_key")sex_key: Int,
            @Field("nickname")nickname: String,
            @Field("head_url")head_url: String,
            @Field("age_range")age_range: Int,
            @Field("phone")phone:String
    ):Flowable<CommonResult>

    /**
     * 是否可以修改昵称
     */
    @POST("sysUser/unauth/isUpdateUserName")
    @FormUrlEncoded
    fun isUpdateUserName(@Field("userId")userId:Int):Flowable<BaseResult<Boolean>>

    /**
     * 店铺详情-拼团
     */
    @GET("issue/unauth/queryBusUser")
    fun queryBusUserCollage(@Query("userId")userId: Int,
                            @Query("type")type: Int):Flowable<ShopGroupBean>

    /** 店铺详情-众购
     */
    @GET("issue/unauth/queryBusUser")
    fun queryBusUserCrowd(@Query("userId")userId: Int,
                          @Query("type")type: Int):Flowable<ShopCrowdBean>

    /** 店铺详情-商品
     */
    @GET("issue/unauth/queryBusUser")
    fun queryBusUserSpu(@Query("userId")userId: Int,
                        @Query("type")type: Int):Flowable<ShopSpuBean>

    /** 店铺详情-数量
     */
    @GET("issue/unauth/queryBusUser")
    fun queryBusUserCount(@Query("userId")userId: Int,
                          @Query("type")type: Int):Flowable<ShopCountBean>

    /**
     * 是否已经拼满
     */
    @POST("colOrder/unauth/isComplete")
    @FormUrlEncoded
    fun isComplete(@Field("tId")tId:Int):Flowable<BaseResult<Boolean>>

    /**
     * 拼团成功分享
     */
    @POST("collage/unauth/shareParam")
    @FormUrlEncoded
    fun shareParam(@Field("collageId")collageId:Int,
                   @Field("userId")userId:Int):Flowable<ShareParamBean>

    @POST("facade/unauth/colCount")
    @FormUrlEncoded
    fun colCount(@Field("userId")userId:Int):Flowable<BaseResult<MainCountBean>>

    @POST("facade/unauth/crowCount")
    @FormUrlEncoded
    fun crowCount(@Field("userId")userId:Int):Flowable<BaseResult<MainCountBean>>

    @POST("facade/unauth/getUserComment")
    @FormUrlEncoded
    fun getUserComment(@Field("page")page:Int,
                       @Field("rows")rows:Int,
                       @Field("relate_id")relate_id:Int,
                       @Field("type")type:Int,
                       @Field("mId")userId:Int):Flowable<PageBean<CommentBean>>


    @POST("facade/unauth/getHostComment")
    @FormUrlEncoded
    fun getHostComment(@Field("relate_id")relate_id:Int,
                       @Field("type")type:Int):Flowable<BaseResult<List<CommentBean>>>

    @GET("app/unauth/addFabulous")
    fun addFabulous(@Query("coId")coId:Int):Flowable<CommonResult>

    @POST("app/unauth/addUserComment")
    @FormUrlEncoded
    fun addUserComment(@Field("type") type: Int,
                       @Field("relate_id") relate_id: Int,
                       @Field("content") content: String,
                       @Field("user_id") user_id: Int,
                       @Field("orderNo") orderNo: String):Flowable<CommonResult>

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
                            @Field("userId")userId:Int ):Flowable<BaseResult<AfterSaleBean>>

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
                               @Field("returnOrderNo")returnOrderNo:String):Flowable<BaseResult<AfterSaleBean>>

    @POST("orderReturn/unauth/getReturnApply")
    @FormUrlEncoded
    fun getReturnApply(@Field("type")type:Int,
                       @Field("orderNo")orderNo:String):Flowable<BaseResult<AfterSaleBean>>


    @POST("orderReturn/unauth/cancelOrderReturnApply")
    @FormUrlEncoded
    fun cancelOrderReturnApply(@Field("applyId")applyId:Int):Flowable<CommonResult>

    @GET("facade/unauth/getLogisList")
    fun getLogisList():Flowable<List<LogisBean>>

    @POST("orderReturn/unauth/addOrderLogis ")
    @FormUrlEncoded
    fun addOrderLogis(@Field("logisCompany")logisCompany:String,
                      @Field("logisNo")logisNo:String,
                      @Field("orderNo")orderNo:String,
                      @Field("wayBillNo")wayBillNo:String):Flowable<CommonResult>

    /**
     * 再次申请售后
     */
    @POST("orderReturn/unauth/againOrderReturnApply")
    @FormUrlEncoded
    fun againOrderReturnApply(@Field("applyId")applyId:Int):Flowable<CommonResult>
}