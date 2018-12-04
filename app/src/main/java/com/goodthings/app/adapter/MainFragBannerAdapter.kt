package com.goodthings.app.adapter

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.goodthings.app.R
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.HomeRecomSubBean
import com.goodthings.app.inerfaces.OnMainFragBannerClickListener
import com.goodthings.app.view.RoundAngleImageView

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/26
 * 修改内容：
 * 最后修改时间：
 */
class MainFragBannerAdapter(val mContex:Context,
                            private val beans: List<HomeRecomSubBean>,
                            private var bannerListener: OnMainFragBannerClickListener?) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
       return view == `object`
    }

    override fun getCount(): Int {
        return Integer.MAX_VALUE
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = View.inflate(mContex, R.layout.item_mainfrag_banner,null)
        var imageView = view.findViewById<RoundAngleImageView>(R.id.mainfrag_banner_imgagview)
        Glide.with(mContex).load(Consts.IMAGEURL+ beans[position%beans.size].coverPic).into(imageView)
        imageView.setOnClickListener {
            bannerListener?.onBannerClick(beans[position%beans.size])
        }
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
       container.removeView(`object` as View?)
    }
}