package com.ashlikun.xviewpager2.transform

import android.os.Build
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.ashlikun.xviewpager2.ViewPagerUtils
import com.ashlikun.xviewpager2.view.XViewPager
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间: 2020/3/20　16:34
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：缩放的ScaleInTransformer
 */
class ScaleInTransformer(var mMinScale: Float = 0.85f) : BasePageTransformer() {

    companion object {
        //默认缩放的中心点位置
        const val DEFAULT_CENTER = 0.5f
    }

    override fun transformPage(view: View, position: Float) {
        super.transformPage(view, position)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.elevation = -abs(position)
        }
        val pageWidth = view.width
        val pageHeight = view.height

        view.pivotY = (pageHeight / 2).toFloat()
        view.pivotX = (pageWidth / 2).toFloat()
        if (position < -1) {
            view.scaleX = mMinScale
            view.scaleY = mMinScale
            if (isVertical()) {
                view.pivotY = pageHeight.toFloat()
            } else {
                view.pivotX = pageWidth.toFloat()
            }
        } else if (position <= 1) {
            if (position < 0) {
                val scaleFactor = (1 + position) * (1 - mMinScale) + mMinScale
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                if (isVertical()) {
                    view.pivotY = pageHeight * (DEFAULT_CENTER + DEFAULT_CENTER * -position)
                } else {
                    view.pivotX = pageWidth * (DEFAULT_CENTER + DEFAULT_CENTER * -position)
                }
            } else {
                val scaleFactor = (1 - position) * (1 - mMinScale) + mMinScale
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                if (isVertical()) {
                    view.pivotY = pageHeight * ((1 - position) * DEFAULT_CENTER)
                } else {
                    view.pivotX = pageWidth * ((1 - position) * DEFAULT_CENTER)
                }
            }
        } else {
            if (isVertical()) {
                view.pivotY = 0f
            } else {
                view.pivotX = 0f
            }
            view.scaleX = mMinScale
            view.scaleY = mMinScale
        }
    }


}