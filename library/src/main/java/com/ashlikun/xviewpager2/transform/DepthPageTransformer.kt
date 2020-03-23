package com.ashlikun.xviewpager2.transform

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/2 17:07
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍： 由小变大的PageTransformer
 */
class DepthPageTransformer
@JvmOverloads
constructor(var mMinScale: Float = 0.75f) : BasePageTransformer() {
    override fun transformPage(view: View, position: Float) {
        super.transformPage(view, position)
        val pageWidth = view.width
        when {
            position < -1 -> {
                // 屏幕左边的左边不显示
                view.alpha = 0f
            }
            position <= 0 -> {
                // [-1,0]
                view.alpha = 1f
                view.translationX = 0f
                view.scaleX = 1f
                view.scaleY = 1f
            }
            position <= 1 -> {
                // (0,1]
                view.alpha = 1 - position
                view.translationX = pageWidth * -position
                val scaleFactor = (mMinScale
                        + (1 - mMinScale) * (1 - Math.abs(position)))
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
            }
            else -> {
                // 屏幕右边的右边不显示
                view.alpha = 0f
            }
        }
    }
}