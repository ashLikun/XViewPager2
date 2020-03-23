package com.ashlikun.xviewpager2.indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import kotlin.math.abs

/**
 * @author　　: 李坤
 * 创建时间: 2020/3/13 13:21
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：平移的Indicator
 */
class TransIndicator @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : IBannerIndicator(context!!, attrs, defStyleAttr) {
    /**
     * 整个移动的距离
     */
    private var moveDistance = 0f

    /**
     * 2个view的距离
     */
    private var moveSize = 0f
    private var firstViewX = 0
    private var currentSelect = 0
    var mPaint: Paint? = null
    override fun initView(context: Context?, attrs: AttributeSet?) {
        clipChildren = false
        clipToPadding = false
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.color = -0x10000
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), Math.max(height,
                Math.max(noSelectDraw!!.intrinsicHeight, selectDraw!!.intrinsicHeight)))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (pointViews.size < 1) {
            return
        }
        firstViewX = pointViews[0].x.toInt()
        moveSize = if (pointViews.size == 1) {
            0f
        } else {
            pointViews[1].x - firstViewX
        }
        moveDistance = firstViewX - Math.abs(selectDraw!!.intrinsicWidth - noSelectDraw!!.intrinsicWidth).toFloat()
    }

    /**
     * 底部指示器资源图片
     *
     * @param
     */
    override fun notifyDataSetChanged(selectIndex: Int): TransIndicator? {
        removeAllViews()
        pointViews.clear()
        if (datas == null) {
            return this
        }
        currentSelect = selectIndex
        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(space, 0, space, 0)
        datas?.forEachIndexed { index, any ->
            // 翻页指示的点
            val pointView: View = ImageView(context)
            pointView.background = noSelectDraw
            pointViews.add(pointView)
            addView(pointView, params)
        }
        return this
    }

    override fun setSelectDraw(selectDraw: Drawable?, selectIndex: Int): IBannerIndicator? {
        selectDraw!!.setBounds(0, 0, selectDraw.intrinsicWidth, selectDraw.intrinsicHeight)
        super.setSelectDraw(selectDraw, selectIndex)
        return this
    }

    override fun onPointScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        moveDistance = if (position == pointViews.size - 1 && positionOffset > 0) {
            //最后一个向第一个移动
            firstViewX - abs(selectDraw!!.intrinsicWidth - noSelectDraw!!.intrinsicWidth).toFloat()
        } else {
            firstViewX - abs(selectDraw!!.intrinsicWidth - noSelectDraw!!.intrinsicWidth) + (positionOffset * moveSize + position * moveSize)
        }
        invalidate()
    }

    override fun onPointSelected(selectIndex: Int) {
        currentSelect = selectIndex
    }

    /**
     * 重绘圆点的运动
     *
     * @param canvas
     */
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        canvas.save()
        canvas.translate(moveDistance, 0f)
        selectDraw!!.draw(canvas)
        canvas.restore()
    }
}