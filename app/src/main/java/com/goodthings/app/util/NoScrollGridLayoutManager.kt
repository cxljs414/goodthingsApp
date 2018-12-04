package com.goodthings.app.util

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

class NoScrollGridLayoutManager : GridLayoutManager {
    var mwidth = 0
    var mheight = 0

    private val mMeasuredDimension = IntArray(2)

    constructor(context: Context, spanCount: Int) : super(context, spanCount) {}

    constructor(context: Context, spanCount: Int,
                orientation: Int, reverseLayout: Boolean) : super(context, spanCount, orientation, reverseLayout) {
    }

    override fun onMeasure(recycler: RecyclerView.Recycler?,
                           state: RecyclerView.State?, widthSpec: Int, heightSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        val widthSize = View.MeasureSpec.getSize(widthSpec)
        val heightSize = View.MeasureSpec.getSize(heightSpec)

        var width = 0
        var height = 0
        val count = itemCount
        val span = spanCount
        for (i in 0 until count) {
            measureScrapChild(recycler, i, View.MeasureSpec.makeMeasureSpec(i,
                    View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i,
                            View.MeasureSpec.UNSPECIFIED), mMeasuredDimension)

            if (orientation == LinearLayoutManager.HORIZONTAL) {
                if (i % span == 0) {
                    width = width + mMeasuredDimension[0]
                }
                if (i == 0) {
                    height = mMeasuredDimension[1]
                }
            } else {
                if (i % span == 0) {
                    height = height + mMeasuredDimension[1]
                }
                if (i == 0) {
                    width = mMeasuredDimension[0]
                }
            }
        }

        when (widthMode) {
            View.MeasureSpec.EXACTLY -> width = widthSize
        }

        when (heightMode) {
            View.MeasureSpec.EXACTLY -> height = heightSize
        }
        mheight = height
        mwidth = width
        setMeasuredDimension(width, height)
    }

    private fun measureScrapChild(recycler: RecyclerView.Recycler?,
                                  position: Int, widthSpec: Int, heightSpec: Int, measuredDimension: IntArray) {
        if (position < itemCount) {
            try {
                val view = recycler!!.getViewForPosition(0)// fix
                // 鍔ㄦ?佹坊鍔犳椂鎶ndexOutOfBoundsException
                if (view != null) {
                    this.measureChild(view, 0, 0)
                    measuredDimension[0] = this.getDecoratedMeasuredWidth(view)
                    measuredDimension[1] = this.getDecoratedMeasuredHeight(view)
                    recycler.recycleView(view)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    class SpacesItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State?) {
            outRect.left = space
            outRect.right = space
            outRect.bottom = space
            outRect.top = space

            // Add top margin only for the first item to avoid double space between items
            //          if(parent.getChildLayoutPosition(view) == 0)
        }
    }

    override fun canScrollVertically(): Boolean {
        return false
    }
}


