package com.goodthings.app.activity.addrlist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.goodthings.app.R
import com.goodthings.app.activity.addaddr.AddAddrActivity
import com.goodthings.app.adapter.AddrListAdapter
import com.goodthings.app.application.Consts
import com.goodthings.app.base.BaseActivity
import com.goodthings.app.bean.AddressBean
import com.goodthings.app.util.ScreenUtil
import com.goodthings.app.util.SpacesItemDecorationtwo
import kotlinx.android.synthetic.main.activity_addr_list.*
import kotlinx.android.synthetic.main.top_bar.*
import kotlinx.coroutines.experimental.newCoroutineContext

class AddrListActivity :
        BaseActivity<AddrListContract.CityListView, AddrListContract.CityListPresenter>(),
        AddrListContract.CityListView{

    override var presenter: AddrListContract.CityListPresenter = AddrListPresenterImpl()
    private var addrListAdapter: AddrListAdapter? = null
    private var isSelectAddr = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addr_list)
        topback.setOnClickListener { onBackPressed() }
        toptitle.text = "管理收货地址"
        isSelectAddr = intent.getBooleanExtra("isSelectAddr",false)
        initAddrList()
        addrlist_add.setOnClickListener {
            startActivityForResult(Intent(this@AddrListActivity,AddAddrActivity::class.java),Consts.REQUEST_ADDADDR)
        }
        presenter.loadData()
    }

    private fun initAddrList() {
        addrlist_recyclerview.layoutManager = LinearLayoutManager(this)
        addrlist_recyclerview.addItemDecoration(
                SpacesItemDecorationtwo(0,
                ScreenUtil.dip2px(this,1f)))
        addrListAdapter = AddrListAdapter(ArrayList())
        addrlist_recyclerview.adapter = addrListAdapter
        addrListAdapter?.setOnItemChildClickListener { adapter, view, position ->
            var addr:AddressBean= adapter.getItem(position) as AddressBean
            when(view.id){
                R.id.itemaddr_root -> {
                    if(isSelectAddr){
                        var intent= Intent()
                        intent.putExtra("addrId",addr.id)
                        intent.putExtra("addrName",addr.name)
                        intent.putExtra("addrPhone",addr.phone)
                        intent.putExtra("addrDetail",addr.city+addr.addr)
                        setResult(Activity.RESULT_OK,intent)
                        finish()
                    }
                }
                R.id.itemaddr_edit -> {
                    var intent = Intent(this@AddrListActivity,AddAddrActivity::class.java)
                    intent.putExtra("addrId",addr.id)
                    startActivityForResult(intent,Consts.REQUEST_ADDADDR)
                }
            }
        }
    }

    override fun showAddrList(it: List<AddressBean>?) {
        addrListAdapter?.setNewData(it)
        addrListAdapter?.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == Consts.REQUEST_ADDADDR){
                presenter.loadData()
            }
        }
    }
}
