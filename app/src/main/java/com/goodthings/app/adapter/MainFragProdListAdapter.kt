package com.goodthings.app.adapter

import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.goodthings.app.R
import com.goodthings.app.activity.main.mainfrag.MainFragContract
import com.goodthings.app.application.Consts
import com.goodthings.app.bean.MainFragProdBean

/**
 * 功能：
 * 示范：
 * 作者：常兴
 * 创建时间：2018/3/5
 * 修改内容：
 * 最后修改时间：
 */
class MainFragProdListAdapter(data: MutableList<MainFragProdBean>?) :
        BaseMultiItemQuickAdapter<MainFragProdBean, BaseViewHolder>(data) {
    private var prodListener: MainFragContract.OnProdClickListener? = null
    var topicPersonClickListener:MainFragContract.OnTopicPersonClickListener? = null
    var collectPersonListener: MainFragContract.OnCollectPersonClickListener? = null
    var onShareListener:MainFragContract.OnProdShareListener? = null
    var onTopicClickListener:MainFragContract.OnTopicClickListener? = null

    init {
        addItemType(Consts.MAIN_TYPE_DEFAULT, R.layout.item_mainfrag_prodlist)
        addItemType(Consts.MAIN_TYPE_BIGIMAGE, R.layout.item_mainfrag_bigimage)
        addItemType(Consts.MAIN_TYPE_NOIMAGE,R.layout.item_mainfrag_noimage)
        addItemType(Consts.MAIN_TYPE_SMALLADVER,R.layout.item_mainfrag_smalladver)
        addItemType(Consts.MAIN_TYPE_BIGADVER, R.layout.item_mainfrag_bigadver)
        addItemType(Consts.MAIN_TYPE_TOPIC,R.layout.item_mainfrag_topic)
        addItemType(Consts.MAIN_TYPE_PERSON,R.layout.item_mainfrag_person)
    }

    override fun convert(helper: BaseViewHolder?, item: MainFragProdBean?) {
        when(helper?.itemViewType){
            Consts.MAIN_TYPE_DEFAULT     -> {
                helper?.setText(R.id.item_mainfrag_default_nickname,item?.nickName)
                Glide.with(mContext).load(Consts.IMAGEURL+item?.headUrl).into(helper?.getView(R.id.item_mainfrag_default_headview))
                if(item?.isAdver!!){
                    helper?.setText(R.id.item_mainfrag_default_title,item?.title)
                    helper?.setVisible(R.id.item_mainfrag_default_collectcount,false)
                    helper?.setVisible(R.id.item_mainfrag_default_share,false)
                    Glide.with(mContext).load(Consts.IMAGEURL+item?.coverPic).into(helper?.getView(R.id.item_mainfrag_default_image))
                }else{
                    helper?.setText(R.id.item_mainfrag_default_title,item?.prdTitle)
                    helper?.setText(R.id.item_mainfrag_default_collectcount,"${item?.collectCount}")
                    helper?.setVisible(R.id.item_mainfrag_default_collectcount,true)
                    helper?.setVisible(R.id.item_mainfrag_default_share,true)
                    Glide.with(mContext).load(Consts.IMAGEURL+item?.coverUrl).into(helper?.getView(R.id.item_mainfrag_default_image))
                    helper.setOnClickListener(R.id.item_mainfrag_default_share, View.OnClickListener {
                        onShareListener?.onProdShare(item)
                    })
                }
                helper.setOnClickListener(R.id.item_mainfrag_default_root, View.OnClickListener { prodListener?.onProkClick(item.isAdver,item!!) })
            }
            Consts.MAIN_TYPE_BIGIMAGE    -> {
                helper?.setText(R.id.item_mainfrag_bigimage_nickname,item?.nickName)
                Glide.with(mContext).load(Consts.IMAGEURL+item?.headUrl).into(helper?.getView(R.id.item_mainfrag_bigimage_headview))
                if(item?.isAdver!!){
                    helper?.setText(R.id.item_mainfrag_bigimage_title,item?.title)
                    Glide.with(mContext).load(Consts.IMAGEURL+item?.coverPic).into(helper?.getView(R.id.item_mainfrag_bigimage_image))
                    helper?.setVisible(R.id.item_mainfrag_bigimage_collectcount,false)
                    helper?.setVisible(R.id.item_mainfrag_bigimage_share,false)
                }else{
                    helper?.setText(R.id.item_mainfrag_bigimage_title,item?.prdTitle)
                    Glide.with(mContext).load(Consts.IMAGEURL+item?.coverUrl).into(helper?.getView(R.id.item_mainfrag_bigimage_image))
                    helper?.setVisible(R.id.item_mainfrag_bigimage_collectcount,true)
                    helper?.setVisible(R.id.item_mainfrag_bigimage_share,true)
                    helper?.setText(R.id.item_mainfrag_bigimage_collectcount,"${item?.collectCount}")
                    helper.setOnClickListener(R.id.item_mainfrag_bigimage_share, View.OnClickListener {
                        onShareListener?.onProdShare(item)
                    })

                }
                helper.setOnClickListener(R.id.item_mainfrag_bitimage_root, View.OnClickListener { prodListener?.onProkClick(item.isAdver,item!!) })

            }
            Consts.MAIN_TYPE_NOIMAGE     -> {
                helper?.setText(R.id.item_noimage_default_nickname,item?.nickName)
                if(item?.isAdver!!){
                    helper?.setText(R.id.item_noimage_default_title,item?.title)
                    helper?.setVisible(R.id.item_noimage_default_collectcount,false)
                    helper?.setVisible(R.id.item_noimage_default_share,false)
                }else{
                    helper?.setText(R.id.item_noimage_default_title,item?.prdTitle)
                    helper?.setText(R.id.item_noimage_default_collectcount,"${item?.collectCount}")
                    helper?.setVisible(R.id.item_noimage_default_collectcount,true)
                    helper?.setVisible(R.id.item_noimage_default_share,true)
                }

                Glide.with(mContext).load(Consts.IMAGEURL+item?.headUrl).into(helper?.getView(R.id.item_noimage_default_headview))
                helper.setOnClickListener(R.id.item_noimage_default_root, View.OnClickListener { prodListener?.onProkClick(item.isAdver,item!!) })
                helper.setOnClickListener(R.id.item_noimage_default_share, View.OnClickListener {
                    onShareListener?.onProdShare(item)
                })

            }
            Consts.MAIN_TYPE_SMALLADVER  -> {
                Glide.with(mContext).load(Consts.IMAGEURL+item?.coverPic).into(helper?.getView(R.id.item_mainfrag_smalladver_image))
                helper.setOnClickListener(R.id.item_mainfrag_smalladver_root, View.OnClickListener {
                    prodListener?.onProkClick(true,item!!)
                })
            }
            Consts.MAIN_TYPE_BIGADVER    -> {
                Glide.with(mContext).load(Consts.IMAGEURL+item?.coverPic).into(helper.getView(R.id.item_mainfrag_bigadver_image))
                helper.setOnClickListener(R.id.item_mainfrag_bigadver_root, View.OnClickListener {
                    prodListener?.onProkClick(true,item!!)
                })
            }
            Consts.MAIN_TYPE_TOPIC       -> {
                var top=helper.getView<RelativeLayout>(R.id.item_mainfrag_topic_top)
                var root=helper.getView<RelativeLayout>(R.id.item_mainfrag_topic_item_root)
                var root1=helper.getView<RelativeLayout>(R.id.item_mainfrag_topic_item_root1)
                var image= helper.getView<ImageView>(R.id.item_mainfrag_topic_item_image)
                var image1= helper.getView<ImageView>(R.id.item_mainfrag_topic_item_image1)
                var title= helper.getView<TextView>(R.id.item_mainfrag_topic_item_title)
                var title1= helper.getView<TextView>(R.id.item_mainfrag_topic_item_title1)
                top.setOnClickListener {
                    onTopicClickListener?.onTopicClick( "/mallPrt/unauth/toPrdList?cateId=0")
                }
                if(item?.place3?.size!! >0){
                    root.visibility = View.VISIBLE
                    var subBean = item?.place3[0]
                    Glide.with(mContext).load(Consts.IMAGEURL+subBean.coverPic).into(image)
                    title.text = subBean.title
                    root.setOnClickListener {
                        topicPersonClickListener?.onTopicPersonClick(true, subBean)
                    }

                    if(item?.place3?.size!! >1){
                        root1.visibility = View.VISIBLE
                        var subBean1 = item?.place3[1]
                        Glide.with(mContext).load(Consts.IMAGEURL+subBean1.coverPic).into(image1)
                        title1.text = subBean1.title
                        root1.setOnClickListener {
                            topicPersonClickListener?.onTopicPersonClick(true, subBean1)
                        }
                    }
                }else{
                    root.visibility = View.GONE
                    root1.visibility = View.GONE
                }
            }
            Consts.MAIN_TYPE_PERSON      -> {
                var container =helper.getView<LinearLayout>(R.id.item_mainfrag_person_layout)
                var index=0
                container.removeAllViews()
                item?.place4?.forEach {
                    var view = View.inflate(mContext,R.layout.item_mainfrag_pserson_item,null)
                    Glide.with(mContext).load(Consts.IMAGEURL+it.coverPic).into(view.findViewById(R.id.item_mainfrag_person_image))
                    var nameText = view.findViewById<TextView>(R.id.item_mainfrag_person_name)
                    nameText.text = it.nickName
                    var follow = view.findViewById<TextView>(R.id.item_mainfrag_person_collect_bt)
                    if(it?.isFocus!! > 0){
                        //已关注
                        follow.text = "取消关注"
                        follow.background = mContext?.resources?.getDrawable(R.drawable.bg_grey_corner_10)
                    }else{
                        //未关注
                        follow.text = "关注"
                        follow.background = mContext?.resources?.getDrawable(R.drawable.bgredcorner10)
                    }
                    val subBean = it
                    follow.setOnClickListener {
                        collectPersonListener?.onCollectPersonClick(subBean.isFocus>0,index,subBean)
                    }

                    var root=view.findViewById<RelativeLayout>(R.id.item_mainfrag_person_root)

                    root.setOnClickListener {
                        topicPersonClickListener?.onTopicPersonClick(false, subBean)
                    }
                    container.addView(view)
                    index +=1
                }
            }
        }

    }

    fun setProdClickListener(listener:MainFragContract.OnProdClickListener){
        this.prodListener = listener
    }


}