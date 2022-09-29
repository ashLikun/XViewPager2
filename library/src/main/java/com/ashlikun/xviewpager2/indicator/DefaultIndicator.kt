package com.ashlikun.xviewpager2.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * @author　　: 李坤
 * 创建时间: 2020/3/13 13:21
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：默认的Indicator，只是一个圆点切换
 */
class DefaultIndicator
@JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : IBannerIndicator(context!!, attrs, defStyleAttr) {
    override fun initView(context: Context?, attrs: AttributeSet?) {}
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = measuredHeight
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height.coerceAtLeast(
            noSelectDraw?.intrinsicHeight?.coerceAtLeast(selectDraw?.intrinsicHeight
                ?: 0)
                ?: 0))
    }
    /**
     * 底部指示器资源图片更新
     */
    override fun notifyDataSetChanged(selectIndex: Int): DefaultIndicator {
        removeAllViews()
        pointViews.clear()
        if (dataCount == 0) {
            return this
        }
        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(space, 0, space, 0)
        (0 until dataCount).forEach { index ->
            // 翻页指示的点
            val pointView: View = ImageView(context)
            if (selectIndex == index) {
                pointView.background = selectDraw
            } else {
                pointView.background = noSelectDraw
            }
            pointViews.add(pointView)
            addView(pointView, params)
        }

        return this
    }

    override fun onPointScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPointSelected(selectIndex: Int) {
        if (selectIndex < pointViews.size) {
            pointViews[selectIndex].background = selectDraw
        }
        for (i in pointViews.indices) {
            if (selectIndex != i) {
                pointViews[i].background = noSelectDraw
            }
        }
    }
}