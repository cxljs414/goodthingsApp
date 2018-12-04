package com.goodthings.app.activity.map

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.amap.api.services.core.PoiItem
import com.goodthings.app.R

class RecyclerViewAdapter(private val mContext: Context,
                          val pois:ArrayList<PoiItem>,
                          val checkedChangeListener: OnCheckIndexChanged)
    : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    var checkIndex:Int = 0
    var isSelect:Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(mContext, R.layout.item_listview, null))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        isSelect = true
        val poi:PoiItem = pois[position]
        holder.nametv.text = poi.title
        holder.snippet.text = poi.snippet
        holder.radio.isChecked = position == checkIndex
        holder.radio.setOnCheckedChangeListener(null)
            holder.radio.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
                override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                    if(isSelect) return
                    if(p1) {
                        checkedChangeListener.onCheckIndexChanged(position)
                    }
                }
            })
        holder.root.setOnClickListener {
            if(!isSelect && checkIndex != position){
                checkedChangeListener.onCheckIndexChanged(position)
            }
        }
        isSelect = false
    }

    override fun getItemCount(): Int {
        return pois.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var root = itemView.findViewById<RelativeLayout>(R.id.root)
        var radio:RadioButton = itemView.findViewById(R.id.radio)
        var nametv = itemView.findViewById<TextView>(R.id.title)
        var snippet = itemView.findViewById<TextView>(R.id.snippet)
    }

    interface OnCheckIndexChanged{
        fun onCheckIndexChanged(position: Int)
    }
}
