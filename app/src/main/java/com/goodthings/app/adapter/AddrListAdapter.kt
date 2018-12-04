package com.goodthings.app.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.bean.AddressBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/5/3
 * 修改内容：
 * 最后修改时间：
 */
class AddrListAdapter(addrList:List<AddressBean>) :
        BaseQuickAdapter<AddressBean,BaseViewHolder>(R.layout.item_addrlist,addrList) {
    override fun convert(helper: BaseViewHolder?, item: AddressBean?) {
        helper?.setText(R.id.itemaddr_name,item?.name)
        helper?.setText(R.id.itemaddr_phone,item?.phone)
        helper?.setText(R.id.itemaddr_detail,item?.addr)
        if(item?.is_default == 1){
            helper?.setVisible(R.id.itemaddr_default, true)
        }else{
            helper?.setVisible(R.id.itemaddr_default, false)
        }

        helper?.addOnClickListener(R.id.itemaddr_root)
        helper?.addOnClickListener(R.id.itemaddr_edit)

    }

}