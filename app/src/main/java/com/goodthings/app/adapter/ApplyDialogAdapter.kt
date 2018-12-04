package com.goodthings.app.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/7/20
 * 修改内容：
 * 最后修改时间：
 */
class ApplyDialogAdapter(data:List<String>,var checkedStr:String) :
        BaseQuickAdapter<String,BaseViewHolder>(R.layout.item_applydialog,data) {

    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.setText(R.id.item_applydialog_content,item)
        helper?.setChecked(R.id.item_applydialog_rb,item == checkedStr)
        helper?.addOnClickListener(R.id.item_applydialog_rb)
    }
}