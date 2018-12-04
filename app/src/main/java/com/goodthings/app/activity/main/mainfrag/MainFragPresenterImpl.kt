package com.goodthings.app.activity.main.mainfrag

import android.content.Intent
import com.goodthings.app.activity.login.LoginActivity
import com.goodthings.app.activity.redpacket.RedPacketActivity
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BasePresenterImpl
import com.goodthings.app.base.MainFragCateBean
import com.goodthings.app.bean.*
import com.goodthings.app.http.ApiManager
import com.goodthings.app.util.RxUtil
import com.goodthings.app.util.SPUtil
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.joda.time.LocalDate

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/21
 * 修改内容：
 * 最后修改时间：
 */
class MainFragPresenterImpl :BasePresenterImpl<MainFragContract.MainFragView>(),
        MainFragContract.MainFragPresenter {

    private var cityCode:String = "110000"
    private var recomBean:HomeRecomBean? = null
    private var cateList:List<MainFragCateBean> = ArrayList()
    private var tabMap:HashMap<Int,Int>? = HashMap()//tab索引对应的page数目
    private var tabDataMap:HashMap<Int,MutableList<MainFragProdBean>>? = HashMap()//tab索引对应的数据列表
    private var isLoading= false
    private var curSelectedPosition:Int= 0

    override fun start() {
        recomBean = null
        tabMap?.clear()
        tabDataMap?.clear()
        cityCode = SPUtil.getStringValue(mView?.getContext()!!,"cityCode","110000")
        requestRecContentList()
        //collageQuery()
        colCount()
        corwdCount()
    }

    override fun reselectedTab(selectedTabPosition: Int) {
        if(selectedTabPosition == curSelectedPosition)return
        curSelectedPosition = selectedTabPosition
        if(tabDataMap?.contains(selectedTabPosition)!!){
            //已经请求过这个tab的数据，无需重新请求，从map中拿出来就是
            mView?.updateProdList(tabDataMap?.get(selectedTabPosition), selectedTabPosition,recomBean)
        }else{
            //没有这个tab的数据，说明没有请求过，需要请求数据
            getProdList(selectedTabPosition,cateList[selectedTabPosition].prdCateId,cityCode,0,false)
        }
    }

    override fun loadMoreData(position: Int) {
        if(isLoading)return
        isLoading = true
        var pageIndex = 0
        if(tabMap?.containsKey(position)!!) {
            pageIndex = tabMap?.get(position)!!
        }
        pageIndex += 1
        getProdList(position,cateList[position].prdCateId,cityCode,pageIndex,true)
    }

    private fun requestRecContentList() {
        mView?.showProgressDialog("正在请求数据...")
        var userId = -1
        var user: User? = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null)userId = user.id
        //检查是否登录
        var cityCode = SPUtil.getStringValue(mView?.getContext()!!,"cityCode","110000")
        ApiManager.getRecContentList(1, LocalDate.now().toString("yyyy-MM-dd"),cityCode,userId)
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<HomeRecomBean>(FragmentEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            tabDataMap?.clear()
                            recomBean = it
                            if(it.place1 != null){
                                mView?.showBanner(it.place1)
                            }
                            if(it.place2 != null){
                                mView?.columnList(it.place2)
                            }

                            mView?.showRecommedNews(it.place5)

                            mView?.showRecommedCrowd(it.place6)

                            mView?.showLeaderIP(it.place7)

                            mView?.showProfessionIP(it.place8)

                            mView?.showRecommedGroups(it.place9)
                            getCateList()
                        },
                        {
                            mView?.showMessage(it.message)
                            mView?.hideProgressDialog()
                        },
                        {
                            mView?.hideProgressDialog()
                        }
                )
    }

    private fun getCateList() {
        //mView?.showProgressDialog("正在请求数据...")
        ApiManager.getCateList(cityCode)
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<List<MainFragCateBean>>(FragmentEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(it.size != cateList.size) {
                                mView?.showCateList(it)
                                cateList = it
                            }

                            var pos= 0
                            it.forEach {
                                tabMap?.put(pos,0)
                                pos++
                            }
                            /*tabDataMap?.put(0, ArrayList())
                            getNotFixContentList(0)*/
                            getProdList(0,it[0].prdCateId,cityCode,0,false)
                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                            mView?.hideProgressDialog()
                        }
                )
    }

    private fun getProdList(index:Int,cateId: Int, cityCode: String, pageIndex: Int,isLoadMore:Boolean) {
        //mView?.showProgressDialog("正在请求数据...")
        ApiManager.getPrdList(cateId,cityCode,pageIndex*20,20)
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<PageBean<MainFragProdBean>>(FragmentEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                    //如果是加载更多，失败之后将page减去1
                    if(isLoadMore){
                        var pageIndex:Int= tabMap!![index]!!
                        tabMap?.put(index,pageIndex--)
                    }
                }
                .subscribe(
                        {
                            if(it?.pageList == null){
                                mView?.updateProdList(ArrayList(), index,recomBean)
                                tabDataMap?.put(index, ArrayList())
                                getNotFixContentList(index)
                                return@subscribe
                            }
                            if(!isLoadMore) {//不是加载更多，直接显示并保存
                                //mView?.updateProdList(it.pageList as MutableList<MainFragProdBean>,index,recomBean)
                                var size= 1
                                it.pageList!!.forEach {
                                    var yu=size%5
                                    if(yu == 1){
                                        it.type = Consts.MAIN_TYPE_DEFAULT
                                    }
                                    if(yu == 2){
                                        it.type = Consts.MAIN_TYPE_DEFAULT
                                    }
                                    if(yu == 3){
                                        it.type = Consts.MAIN_TYPE_NOIMAGE
                                    }
                                    if(yu == 4){
                                        it.type = Consts.MAIN_TYPE_DEFAULT
                                    }
                                    if(yu == 0){
                                        it.type = Consts.MAIN_TYPE_BIGIMAGE
                                    }
                                    size+=1
                                }
                                tabDataMap?.put(index, (it.pageList as MutableList<MainFragProdBean>?)!!)
                                getNotFixContentList(index)
                            }else{//加载更多，追加到之后,并显示全部
                                if(index ==0 || it.pageList?.size == 0){
                                    //mView?.showMessage("没有更多了")
                                    mView?.noMoreData()
                                    var pageIndex:Int= tabMap!![index]!!
                                    tabMap?.put(index,pageIndex--)
                                    return@subscribe
                                }
                                var moreList= tabDataMap?.get(index)
                                moreList?.addAll(it.pageList!!)
                                mView?.updateProdList(moreList, index,recomBean)
                                tabDataMap?.remove(index)
                                tabDataMap?.put(index,moreList!!)
                                tabMap?.put(index,pageIndex)
                            }
                            isLoading = false
                        },
                        {
                            if(it?.message.isNullOrEmpty()) {
                                mView?.showMessage(it?.message)
                                mView?.noMoreData()
                            }
                            isLoading = false
                        },
                        {
                        }
                )
    }

    private fun getNotFixContentList(position:Int){
        //mView?.showProgressDialog("正在请求数据...")
        ApiManager.getNotFixContentList(cateList[position].cateId,cityCode,LocalDate.now().toString("yyyy-MM-dd"))
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<List<MainFragProdBean>>(FragmentEvent.DESTROY))
                .doOnError {
                    mView?.hideProgressDialog()
                }
                .subscribe(
                        {
                            if(tabDataMap?.containsKey(position)!!) {
                                var advirMap:LinkedHashMap<Int,MutableList<MainFragProdBean>>? = LinkedHashMap()
                                var advirIndex= 0
                                it.forEach {
                                    it.isAdver = true
                                    if(advirMap?.containsKey(it.sort-1)!!){
                                        advirMap?.get(it.sort-1)?.add(it)
                                    }else{
                                        var advertList = ArrayList<MainFragProdBean>()
                                        advertList.add(it)
                                        advirMap?.put(it.sort-1,advertList)
                                    }
                                    advirIndex+=1
                                }
                                var prodList = ArrayList<MainFragProdBean>()
                                if(position != 0) {
                                    prodList.addAll(tabDataMap?.get(position)!!)
                                }
                                tabDataMap?.remove(position)

                                var allList = ArrayList<MainFragProdBean>()
                                var prodIndex= 0
                                prodList?.forEach {
                                    if(advirMap?.containsKey(prodIndex)!!){
                                        allList.addAll(advirMap[prodIndex]!!)
                                        advirMap.remove(prodIndex)
                                    }
                                    allList?.add(it)
                                    prodIndex+=1
                                }

                                advirMap?.forEach {
                                    allList.addAll(it.value)
                                }

                                if(position == 0) {
                                    if (recomBean?.place3 != null) {
                                        var topicData= MainFragProdBean(0,"","",0,"","",5,0,0,"",0,"","",0,0,"",5,"",ArrayList(),ArrayList())
                                        topicData.type = 5
                                        topicData.place3 = recomBean?.place3!!
                                        if(allList?.size!! > 6){
                                            allList.add(6,topicData)
                                        }else{
                                            allList.add(topicData)
                                        }
                                    }
                                    if (recomBean?.place4 != null) {
                                        var personData= MainFragProdBean(0,"","",0,"","",5,0,0,"",0,"","",0,0,"",5,"",ArrayList(),ArrayList())
                                        personData.type = 6
                                        personData.place4 = recomBean?.place4!!
                                        if(allList?.size!! > 18){
                                            allList.add(18,personData)
                                        }else{
                                            allList.add(personData)
                                        }
                                    }
                                }



                                tabDataMap?.put(position,allList)
                                /*tabDataMap?.remove(position)
                                tabDataMap?.put(position,prodList!!)*/
                                mView?.updateProdList(allList, position,recomBean)

                            }


                        },
                        {
                            mView?.showMessage(it.message)
                        },
                        {
                        }
                )
    }

    override fun tyDailyRedEnvelopes() {
        var user: User? = SPUtil.getUserBean(mView?.getContext()!!)
        if(user != null) {
            ApiManager.tyDailyRedEnvelopes(user.id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(fragmentLifecycle?.bindUntilEvent<CommonResult>(FragmentEvent.DESTROY))
                    .subscribe(
                            {
                                if (it.code == 2000) {
                                    mView?.getActivity()?.startActivity(Intent(mView?.getContext(),RedPacketActivity::class.java))
                                } else {
                                    var intent = Intent(mView?.getContext(), RedPacketActivity::class.java)
                                    intent.putExtra("hongbao",it.msg)
                                    mView?.getActivity()?.startActivity(intent)
                                }
                            },
                            {
                                mView?.showMessage(it.message)
                            },
                            {
                            }
                    )
        }else{
            mView?.getActivity()?.startActivity(Intent(mView?.getContext(),LoginActivity::class.java))
        }
    }

    private fun collageQuery(){
        ApiManager.collageQuery(1 ,2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(fragmentLifecycle?.bindUntilEvent<PageBean<GroupListBean>>(FragmentEvent.DESTROY))
                .doOnError {
                }.subscribe({
                    if(it?.pageList == null || it?.pageList!!.isEmpty()){
                        mView?.noMoreData()
                        return@subscribe
                    }
                    mView?.notifyGroupBuyListUpdate(it.pageList!!)
                },{
                })
    }

    private fun colCount(){
        ApiManager.colCount()
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<MainCountBean>(FragmentEvent.DESTROY))
                .doOnError {
                }.subscribe({
                    mView?.notifyColCount(it)
                },{
                })
    }

    private fun corwdCount(){
        ApiManager.crowCount()
                .compose(RxUtil.hanlderBaseResult())
                .compose(fragmentLifecycle?.bindUntilEvent<MainCountBean>(FragmentEvent.DESTROY))
                .doOnError {
                }.subscribe({
                    mView?.notifyCrowCount(it)
                },{
                })
    }
}