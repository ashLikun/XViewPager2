package com.ashlikun.xviewpager2.transform

import android.util.Log
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间: 2020/3/20　14:26
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：下沉渐变效果
 */
class ZoomOutPageTransformer
@JvmOverloads
constructor(var mMinScale: Float = 0.85f, var mMinAlpha: Float = 0.5f)
    : BasePageTransformer() {

    override fun transformPage(view: View, position: Float) {
        super.transformPage(view, position)
        val pageWidth = view.width
        val pageHeight = view.height
        when {
            position < -1 -> { // [-Infinity,-1)
                view.alpha = 0f
            }
            position < 1 -> { // [-1,1]
                // 退出画面
                val scaleFactor = mMinScale.coerceAtLeast(1 - abs(position))
                val vertMargin = pageHeight * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                //移动
                if (position < 0) {
                    view.translationX = horzMargin - vertMargin / 2
                } else {
                    view.translationX = -horzMargin + vertMargin / 2
                }
                //缩放
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                // 透明度
                view.alpha = mMinAlpha + (1 - abs(position)) * (1 - mMinAlpha)

            }
            else -> { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.alpha = 0f
            }
        }
    }
}