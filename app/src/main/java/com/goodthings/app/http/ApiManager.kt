package com.goodthings.app.http

import android.annotation.SuppressLint
import android.content.Context
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.goodthings.app.application.Consts
import com.goodthings.app.base.MainFragCateBean
import com.goodthings.app.bean.*
import io.reactivex.Flowable
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


@SuppressLint("StaticFieldLeak")
/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2017/12/25
 * 修改内容：
 * 最后修改时间：
 */
object ApiManager {

    private var mApiService:ApiService
    private var mApiDownLoadService:ApiService
    private var mResponseService:ApiResService
    private var context:Context?= null
    private var cookieJar:PersistentCookieJar? = null
    init {
        val regrofit = initRetrofit()
        mApiService = regrofit.create(ApiService::class.java)
        val downRetrofit = initDownRetrofit()
        mApiDownLoadService = downRetrofit.create(ApiService::class.java)
        var responseRetrofit = initResRetrofit()
        mResponseService = responseRetrofit.create(ApiResService::class.java)
    }

    fun setContext(mContext:Context){
        this.context = mContext
        this.cookieJar = PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
    }

    fun clearCookie(){
        this.cookieJar?.clear()
    }

    /**
     * 下载文件的服务
     */
    private fun initDownRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            networkInterceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                        .method(original.method(), original.body())
                        .addHeader("User-Agent","Android")
                        .build()
                chain.proceed(request)
            })
            addInterceptor(ProgressInterceptor())
            addInterceptor(interceptor)
        }
        return Retrofit.Builder()
                .baseUrl(Consts.URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(client.build())
                .build()
    }

    /**
     * 初始话retrofit
     */
    private fun initRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            networkInterceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                        .method(original.method(), original.body())
                        .addHeader("User-Agent","Android")
                        .build()
                chain.proceed(request)
            })
            addInterceptor(interceptor)
        }
        var okHttpClient:OkHttpClient
        if(context != null){
            okHttpClient= client.cookieJar(cookieJar).build()
        }else{
            okHttpClient = client.build()
        }

        var moshiConverterFactory = MoshiConverterFactory.create()
        moshiConverterFactory.asLenient()
        return Retrofit.Builder()
                .baseUrl(Consts.URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .build()

    }


    /**
     * 初始话retrofit
     */
    private fun initResRetrofit(): Retrofit {
        var okHttpClient:OkHttpClient = OkHttpClient.Builder().build()
        return Retrofit.Builder()
                .baseUrl(Consts.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                // .client(okHttpClient)
                .build()

    }

    fun checkVersion():Flowable<BaseResult<UpdateAppBean>>{
        return mApiService.checkVersion()
    }

    fun requestGuide(): Flowable<BaseResult<GuideBean>> {
        return mApiService.requestGuide()
    }

    fun downLoadAPK(url:String):Flowable<ResponseBody>{
        return mApiDownLoadService.download(url)
    }

    fun queryUserByPhone(phone: String): Flowable<BaseResult<User>> {
        return mApiService.queryUserByPhone(phone)
    }

    fun setLoginUserCache(userId:Int):Flowable<CommonResult>{
        return mApiService.loginUserCache(userId)
    }

    fun sendVerifCode(phone: String):Flowable<BaseResult<String>>{
        return mApiService.sendVerifCode(phone)
    }

    fun loginbyPassword(phone: String,psd:String): Flowable<CommonResult> {
        return mApiService.loginbyPassword(phone,psd)
    }

    fun updatePassword(phone: String,psd: String):Flowable<CommonResult> {
        return mApiService.updatePassword(phone,psd)
    }

    fun uploadimage(imgString: String): Flowable<BaseResult<String>> {
        return mApiService.uploadimage(imgString)
    }

    fun registPsersonInfo(headurl: String, nickname: String, sexKey: String?, age: String?, phone: String?, passwod: String?): Flowable<BaseResult<RegistUserBean>> {
        return mApiService.registPsersonInfo(headurl,nickname,sexKey,age,"0",phone,passwod)
    }

    fun firstRegist(phone: String, password: String): Flowable<CommonResult> {
        return mApiService.firstRegist(phone,password)
    }

    fun guessYouLike(isLogined:Boolean ,userId: Int): Flowable<BaseResult<List<HomeGussLikeBean>>> {
        if(isLogined){
            return mApiService.guessYouLikeByUserId(userId)
        }
        return mApiService.guessYouLike()
    }

    fun getRecContentList(pageId: Int, curDate: String, cityCode: String,userId: Int): Flowable<BaseResult<HomeRecomBean>> {
        return mApiService.getRecContentList(pageId,curDate,cityCode,userId)
    }

    fun getNewsList(): Flowable<BaseResult<List<HomeNewsBean>>> {
        return mApiService.getNewsList()
    }

    fun menuHasNewMsg(): Flowable<BaseResult<Int>> {
        return mApiService.getMsgCount()
    }

    fun getCityCode(city: String): Flowable<BaseResult<String>> {
        return mApiService.getCityCode(city)
    }

    fun getCateList(cityCode: String): Flowable<BaseResult<List<MainFragCateBean>>> {
        return mApiService.getCateList(cityCode)
    }

    fun getPrdList(cateId:Int,cityCode:String,startIndex:Int,pageSize:Int): Flowable<BaseResult<PageBean<MainFragProdBean>>> {
        return mApiService.getPrdList(cateId,cityCode,startIndex,pageSize)
    }

    fun getNotFixContentList(cateId:Int,cityCode:String,curDate:String): Flowable<BaseResult<List<MainFragProdBean>>> {
        return mApiService.getNotFixContentList(1,cateId,cityCode,curDate)
    }

    fun addfollow(userId:Int,follow_user_id: Int): Flowable<CommonResult> {
        return mApiService.addfollow(userId,follow_user_id)
    }

    fun delonefollow(userId:Int,follow_user_id: Int): Flowable<CommonResult> {
        return mApiService.delonefollow(userId,follow_user_id)
    }

    fun tyDailyRedEnvelopes(userId: Int): Flowable<CommonResult> {
        return mApiService.tyDailyRedEnvelopes(userId)
    }

    fun dailyRedEnvelopes(userId:Int): Flowable<CommonResult> {
        return mApiService.dailyRedEnvelopes(userId)
    }

    fun getMyWallet(userId: Int): Flowable<BaseResult<WalletBean>> {
        return mApiService.getMyWallet(userId)
    }

    fun queryCoinRecord(userId: Int,page:Int): Flowable<PageBean<GoldBean>> {
        return mApiService.queryCoinRecord(userId,page,10)
    }

    fun queryCoinSeleRecord(userId: Int,page:Int): Flowable<PageBean<GoldBean>> {
        return mApiService.queryCoinSeleRecord(userId,page,10)
    }

    fun registShareGoldEnvelopes(userId:Int): Flowable<BaseResult<Int>> {
        return mApiService.registShareGoldEnvelopes(userId)
    }

    fun userShareGoldEnvelopes(userId:Int): Flowable<CommonResult> {
        return mApiService.userShareGoldEnvelopes(userId)
    }

    fun shareGoldEnvelopes(userId:Int,cententId:Int,type:Int): Flowable<CommonResult> {
        return mApiService.shareGoldEnvelopes(userId,cententId,type)
    }

    fun share(userId: Int,relate_id:Int,type:String): Flowable<CommonResult> {
        return mApiService.share(userId,relate_id,type)
    }

    fun getCitys(): Flowable<BaseResult<List<CityBean>>> {
        return mApiService.getCitys()
    }

    fun cityson(id:Int): Flowable<BaseResult<List<SysCityArea>>> {
        return mApiService.cityson(id)
    }

    fun crowdQuery(page: Int): Flowable<PageBean<ProdCrowdBean>> {
        return mApiService.crowdQuery(page,20)
    }

    fun crowdQueryById(prodId: Int): Flowable<BaseResult<ProdCrowdBean>> {
        return mApiService.crowdDetail(prodId)
    }

    fun querySkillUser(userId: Int,follow_userId: Int): Flowable<BaseResult<ScrowdUserBean>> {
        return mApiService.querySkillUser(userId,follow_userId)
    }

    fun collectCount(type:Int,relate_id:Int): Flowable<BaseResult<Int>> {
        return mApiService.collectCount(type,relate_id)
    }

    //3产品筹 4 拼团
    fun shareCount(type:Int,relate_id:Int): Flowable<BaseResult<Int>> {
        return mApiService.shareCount(type,relate_id)
    }

    fun getallmsg(userId: Int,skill_id:Int,type:Int,relate_id:Int,follow_user_id:Int): Flowable<BaseResult<FollowBean>> {
        return mApiService.getallmsg(userId,skill_id,type,relate_id,follow_user_id)
    }

    fun addCollect(userId:Int,type:Int,relate_id: Int): Flowable<CommonResult> {
        return mApiService.addcollect(userId,type,relate_id)
    }

    fun deloneCollect(userId: Int,type:Int,relate_id: Int): Flowable<CommonResult> {
        return mApiService.delonecollect(userId,type,relate_id)
    }

    fun queryDefaultAddr(userId:Int): Flowable<BaseResult<AddressBean>> {
        return mApiService.queryDefaultAddr(userId)
    }

    fun addOrder(transAmt:Double,fee:Double,cfId:Int,
                 cfExtId:Int,buyNum:Int,adrId:Int,userId: Int): Flowable<CommonResult> {
        return mApiService.addOrder(1,transAmt,fee,cfId,cfExtId,buyNum,adrId,userId)
    }

    fun getWxPayInit(userId: Int,orderNo:String,type:Int): Flowable<BaseResult<WxPayInitBean>> {
        return mApiService.getWxPayInit(orderNo,type)
    }

    fun addAddress(userId:Int,name:String,phone:String,city:String,addr:String,city_code:String,is_default:Int): Flowable<CommonResult> {
        return mApiService.addAddress(userId,name,phone,city,addr,city_code,is_default)
    }

    fun payQueryByid(userId: Int,crowdId:Int): Flowable<BaseResult<Int>> {
        return mApiService.payQueryByid(userId,crowdId)
    }

    fun shareCrowdRedEnvelopes(userId: Int): Flowable<CommonResult> {
        return mApiService.shareCrowdRedEnvelopes(userId)
    }

    fun shareCrowdEnvelopes(id: Int): Flowable<BaseResult<Int>> {
        return mApiService.shareCrowdEnvelopes(id)
    }

    fun getCrowdOrderlist(userId: Int,page: Int,status:Int): Flowable<PageBean<CrowdOrderBean>> {
        return mApiService.getCrowdOrderlist(userId,10,page,status)
    }

    fun qxCfOrder(orderNo:String,count:Int,eid:Int,status:Int): Flowable<CommonResult> {
        return mApiService.qxCfOrder(orderNo,count,eid,status)
    }

    fun getCfOrderByOrderNo(orderNo:String): Flowable<BaseResult<CrowdOrderBean>> {
        return mApiService.getCfOrderByOrderNo(orderNo)
    }

    fun cfDailyGoldEnvelopes(userId: Int,crowdId: Int): Flowable<BaseResult<Int>> {
        return mApiService.cfDailyGoldEnvelopes(userId,crowdId)
    }

    fun addBrowseLog(userId: Int,crowdId: Int): Flowable<CommonResult> {
        return mApiService.addBrowseLog(userId,3,crowdId)
    }

    fun queryMyAddress(userId: Int): Flowable<BaseResult<List<AddressBean>>> {
        return mApiService.queryMyAddress(userId)
    }

    fun getAddress(addrId:Int): Flowable<BaseResult<AddressBean>> {
        return mApiService.getAddress(addrId)
    }

    fun updateAddress(addrId: Int,
                      user_id:Int,
                      name:String,
                      phone:String,
                      city:String,
                      addr:String,
                      city_code:String,
                      is_default:Int): Flowable<CommonResult> {
        return mApiService.updateAddress(addrId,user_id,name,phone,city,addr,city_code,is_default)
    }

    fun collageQuery(page:Int,rows:Int): Flowable<PageBean<GroupListBean>> {
        return mApiService.collageQuery(page,rows)
    }

    fun tjCollageQuery(): Flowable<List<GroupListBean>> {
        return mApiService.tjCollageQuery(1,10)
    }

    fun getCollageDetail(groupId:Int): Flowable<BaseResult<GroupBuyDetailBean>> {
        return mApiService.getCollageDetail(groupId)
    }

    fun colTeamQuery(collageId:Int): Flowable<List<GroupingBean>> {
        return mApiService.colTeamQuery(collageId)
    }

    fun addGroupOrder(transAmt:Double,
                      fee:Double,
                      colId:Int,
                      skuDetailId:Int,
                      buyNum:Int,
                      adrId:Int,
                      userId: Int,
                      teamid:Int): Flowable<CommonResult> {
        if(teamid == -1){
            return mApiService.addGroupOrderNomal(transAmt,fee,colId,skuDetailId,buyNum,adrId,userId)
        }else{
            return mApiService.addGroupOrderTeam(transAmt,fee,colId,skuDetailId,buyNum,adrId,userId,teamid)
        }

    }

    fun  myCollageOrderlist(userId: Int,page: Int,rows: Int,status:Int): Flowable<PageBean<GroupOrderBean>> {
        return mApiService.myCollageOrderlist(userId,page,rows,status)
    }

    fun qxCollageOrder(old_status:Int,orderId:Int): Flowable<CommonResult> {
        return mApiService.qxCollageOrder(old_status,orderId)
    }

    fun gerCollageOrderDetail(oId:Int): Flowable<BaseResult<GroupOrderDetailBean>> {
        return mApiService.gerCollageOrderDetail(oId)
    }

    fun getCollageOrderDetailByOrderNo(orderNo: String): Flowable<BaseResult<GroupOrderDetailBean>> {
        return mApiService.getCollageOrderDetailByOrderNo(orderNo)
    }

    fun isBuy(userId: Int,cId:Int): Flowable<BaseResult<Boolean>> {
        return mApiService.isBuy(userId,cId)
    }

    fun getPrdDetail(prodId:Int): Flowable<List<ProdBuyDetailBean>> {
        return mApiService.getPrdDetail(prodId)
    }

    fun updateUserInfo(userId: Int,sexKey:Int,nickname: String,headurl: String,ageKey:Int,phone:String): Flowable<CommonResult> {
        return mApiService.updateUserInfo(userId,sexKey,nickname,headurl,ageKey,phone)
    }

    fun isUpdateUserName(userId: Int): Flowable<BaseResult<Boolean>> {
        return mApiService.isUpdateUserName(userId)
    }

    fun queryBusUserCollage(userId: Int): Flowable<ShopGroupBean> {
        return mApiService.queryBusUserCollage(userId,1)
    }

    fun queryBusUserCrowd(userId: Int): Flowable<ShopCrowdBean> {
        return mApiService.queryBusUserCrowd(userId,2)
    }
    fun queryBusUserSpu(userId: Int): Flowable<ShopSpuBean> {
        return mApiService.queryBusUserSpu(userId,3)
    }
    fun queryBusUserCount(userId: Int): Flowable<ShopCountBean> {
        return mApiService.queryBusUserCount(userId,4)
    }

    fun isComplete(tId:Int): Flowable<BaseResult<Boolean>> {
        return mApiService.isComplete(tId)
    }

    fun shareParam(cId:Int,userId: Int): Flowable<ShareParamBean> {
        return mApiService.shareParam(cId,userId)
    }

    fun colCount(): Flowable<BaseResult<MainCountBean>> {
        return mApiService.colCount(0)
    }

    fun crowCount(): Flowable<BaseResult<MainCountBean>> {
        return mApiService.crowCount(0)
    }

    fun getUserComment(page:Int,rows:Int,relate_id: Int,type:Int,mId:Int): Flowable<PageBean<CommentBean>> {
        return mApiService.getUserComment(page,rows,relate_id,type,mId)
    }

    fun getHostComment(relate_id: Int,type: Int): Flowable<BaseResult<List<CommentBean>>> {
        return mApiService.getHostComment(relate_id,type)
    }

    fun addFabulous(colId: Int): Flowable<CommonResult> {
        return mApiService.addFabulous(colId)
    }

    fun addUserComment(type: Int, relate_id: Int, content: String, userId: Int, orderNo: String): Flowable<CommonResult> {
        return mApiService.addUserComment(type,relate_id,content,userId,orderNo)
    }

    fun addOrderReturnApply(type:Int,orderNo:String,applyType:Int,goodsState:Int,reason:String,
                            returnAmt:String,remark:String,phone:String,picUrl:String,
                            userId:Int ):Flowable<BaseResult<AfterSaleBean>>{
        return mApiService.addOrderReturnApply(type,orderNo,applyType,goodsState,
                reason,returnAmt,remark,phone,picUrl,userId)
    }

    fun updateOrderReturnApply(type:Int,orderNo:String,applyType:Int,goodsState:Int,reason:String,
                            returnAmt:String,remark:String,phone:String,picUrl:String,
                            userId:Int,id:Int,returnOrderNo:String ):Flowable<BaseResult<AfterSaleBean>>{
        return mApiService.updateOrderReturnApply(type,orderNo,applyType,goodsState,
                reason,returnAmt,remark,phone,picUrl,userId,id,returnOrderNo)
    }

    fun updateOrderReturnApplyRes(type:Int,orderNo:String,applyType:Int,goodsState:Int,reason:String,
                               returnAmt:String,remark:String,phone:String,picUrl:String,
                               userId:Int,id:Int,returnOrderNo:String ): Call<String> {
        return mResponseService.updateOrderReturnApply(type,orderNo,applyType,goodsState,
                reason,returnAmt,remark,phone,picUrl,userId,id,returnOrderNo)
    }

    fun addOrderReturnApplyRes(type:Int,orderNo:String,applyType:Int,goodsState:Int,reason:String,
                                  returnAmt:String,remark:String,phone:String,picUrl:String,
                                  userId:Int ): Call<String> {
        return mResponseService.addOrderReturnApply(type,orderNo,applyType,goodsState,
                reason,returnAmt,remark,phone,picUrl,userId)
    }

    fun getReturnApply(type:Int,orderNo: String): Flowable<BaseResult<AfterSaleBean>> {
        return mApiService.getReturnApply(type,orderNo)
    }

    fun  cancelOrderReturnApply(applyId:Int): Flowable<CommonResult> {
        return mApiService.cancelOrderReturnApply(applyId)
    }

    fun getLogisList(): Flowable<List<LogisBean>> {
        return mApiService.getLogisList()
    }

    fun addOrderLogis(logisCompany:String,logisNo:String,orderNo:String,wayBillNo:String): Flowable<CommonResult> {
        return mApiService.addOrderLogis(logisCompany,logisNo,orderNo,wayBillNo)
    }

    fun queryLogisDetail(shipperCode:String,billNo:String): Call<String> {
        return mResponseService.queryLogisDetail(shipperCode,billNo)
    }

    fun againOrderReturnApply(appolyId: Int): Flowable<CommonResult> {
        return mApiService.againOrderReturnApply(appolyId)
    }
}

