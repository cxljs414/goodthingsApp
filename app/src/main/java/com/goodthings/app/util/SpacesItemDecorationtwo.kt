package com.goodthings.app.util

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class SpacesItemDecorationtwo : RecyclerView.ItemDecoration {
    private var left: Int = 0
    private var right: Int = 0
    private var top: Int = 0
    private var bottom: Int = 0

    constructor(leftRight: Int, topBottom: Int) {
        this.left = leftRight
        this.right = leftRight
        this.top = topBottom
        this.bottom = topBottom
    }

    constructor(left: Int, right: Int, top: Int, bottom: Int) {
        this.left = left
        this.right = right
        this.top = top
        this.bottom = bottom
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val layoutManager = parent.layoutManager as LinearLayoutManager
        //竖直方向的
        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            //最后一项需要 bottom
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.bottom = bottom
            }
            outRect.top = top
            outRect.left = left
            outRect.right = right
        } else {
            //最后一项需要right
            if (parent.getChildAdapterPosition(view) == layoutManager.itemCount - 1) {
                outRect.right = right
            }
            outRect.top = top
            outRect.left = left
            outRect.bottom = bottom
        }
    }


}



