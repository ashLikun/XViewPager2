package com.ashlikun.xviewpager2.transform

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/2 17:09
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：魅族的滚动效果
 */
class MzTransformer
@JvmOverloads
constructor(var mMinScale: Float = 0.8f, var mMaxScale: Float = 1f) : BasePageTransformer() {

    override fun transformPage(view: View, position: Float) {
        super.transformPage(view, position)
        //setScaleY只支持api11以上
        when {
            position < -1 -> {
                view.scaleY = mMinScale
            }
            position <= 1 -> {
                // [-1,1]
                val scaleFactor = mMinScale + (1 - abs(position)) * (mMaxScale - mMinScale)
                view.scaleY = scaleFactor
            }
            else -> { // (1,+Infinity]
                view.scaleY = mMinScale
            }
        }
    }


}