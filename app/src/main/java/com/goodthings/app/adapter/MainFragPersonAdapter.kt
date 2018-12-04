package com.goodthings.app.adapter

import android.widget.CheckBox
import android.widget.CompoundButton
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.activity.main.mainfrag.MainFragContract
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.HomeRecomSubBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/27
 * 修改内容：
 * 最后修改时间：
 */
class MainFragPersonAdapter(data:List<HomeRecomSubBean>) :
        BaseQuickAdapter<HomeRecomSubBean,BaseViewHolder>(R.layout.item_mainfrag_pserson_item,data) {
    var collectPersonListener: MainFragContract.OnCollectPersonClickListener? = null
    override fun convert(helper: BaseViewHolder?, item: HomeRecomSubBean?) {
        var count = itemCount
        Glide.with(mContext).load(Consts.IMAGEURL+item?.coverPic).into(helper?.getView(R.id.item_mainfrag_person_image))
        helper?.setText(R.id.item_mainfrag_person_name,item?.nickName)
        helper?.addOnClickListener(R.id.item_mainfrag_person_root)
        var checkBox = helper?.getView<CheckBox>(R.id.item_mainfrag_person_collect_bt)
        checkBox?.isChecked = (item?.isFocus!! >= 0)
        checkBox?.setOnCheckedChangeListener { _, isChecked ->
            collectPersonListener?.onCollectPersonClick(isChecked,helper?.position!!,item!!)
        }
    }
}