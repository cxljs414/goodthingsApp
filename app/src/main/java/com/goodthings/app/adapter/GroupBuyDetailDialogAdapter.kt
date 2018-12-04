package com.goodthings.app.adapter

import android.os.CountDownTimer
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.GroupingBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/30
 * 修改内容：
 * 最后修改时间：
 */
class GroupBuyDetailDialogAdapter(data:List<GroupingBean>):
        BaseQuickAdapter<GroupingBean, GroupBuyDetailDialogAdapter.GroupBuyViewHolder>(R.layout.item_groupbuy_detail_dialog,data){

    private var countDownMap:SparseArray<CountDownTimer> = SparseArray()

    override fun convert(helper: GroupBuyViewHolder?, item: GroupingBean?) {
        helper?.setText(R.id.item_groupbuy_detail_dialog_nickname,item?.nickname)
        Glide.with(mContext).load(Consts.IMAGEURL+item?.head_url).into(helper?.getView(R.id.item_groupbuy_detail_dialog_headpic))

        helper?.timeText?.text = covertTime((item?.create_time!!.toLong() + 24*60*60*2*1000) - System.currentTimeMillis())

        var countDownTimer:CountDownTimer? = null
        if(helper?.timeText?.hashCode() != null){
            countDownTimer= countDownMap[helper?.timeText?.hashCode()!!]
            if(countDownTimer != null){
                countDownTimer.cancel()
            }
        }

        var eTime= (item.create_time.toLong() + 24*60*60*2*1000) - System.currentTimeMillis()
        if(eTime > 0) {
            countDownTimer = object : CountDownTimer(eTime, 1000) {
                override fun onTick(millis: Long) {
                    helper?.timeText!!.text = covertTime(millis)
                }

                override fun onFinish() {
                    helper?.timeText!!.text = "结束"
                }

            }
            countDownTimer.start()
            if(helper?.timeText?.hashCode() != null){
                countDownMap.put(helper?.timeText!!.hashCode(),countDownTimer)
            }

        }else{
            helper?.timeText!!.text = "结束"
        }

        helper?.addOnClickListener(R.id.item_groupbuy_detail_dialog_ctbt)

    }

    class GroupBuyViewHolder(view: View?) : BaseViewHolder(view) {
        var timeText:TextView? = view?.findViewById(R.id.item_groupbuy_detail_dialog_downtime)
    }

    fun destory(){
        if(countDownMap == null)return
        for(i in 0 until countDownMap.size()){
            countDownMap[countDownMap.keyAt(i)]?.cancel()
        }
    }

    fun covertTime(millis:Long):String{
        var seconds = millis/1000
        var mins= seconds/60
        var eSeconds=seconds%60
        var hours = mins/60
        var eMins= mins%60
        return "剩余$hours:$eMins:$eSeconds"
    }
}