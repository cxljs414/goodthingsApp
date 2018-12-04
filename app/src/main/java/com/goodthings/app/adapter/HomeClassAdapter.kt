package com.goodthings.app.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.bean.HomeClassBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/3
 * 修改内容：
 * 最后修改时间：
 */
class HomeClassAdapter(layoutResId: Int, data: MutableList<HomeClassBean>?) : BaseQuickAdapter<HomeClassBean, BaseViewHolder>(layoutResId, data) {
    override fun convert(helper: BaseViewHolder?, item: HomeClassBean?) {
        helper?.setText(R.id.item_class_name,item?.title)
        helper?.setImageResource(R.id.item_class_img,item?.image!!)
        helper?.setText(R.id.item_class_content,item?.content)
        helper?.setBackgroundRes(R.id.item_class_root,item?.background!!)
        helper?.addOnClickListener(R.id.item_class_root)
    }
}