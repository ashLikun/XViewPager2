package com.ashlikun.xviewpager2.transform

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.ashlikun.xviewpager2.ViewPagerUtils
import com.ashlikun.xviewpager2.view.XViewPager

/**
 * 作者　　: 李坤
 * 创建时间: 2020/3/20　17:34
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：基础的PageTransformer
 */
open class BasePageTransformer : ViewPager2.PageTransformer {
    protected var mViewPager: XViewPager? = null

    override fun transformPage(page: View, position: Float) {
        if (mViewPager == null) {
            initViewPager(ViewPagerUtils.getXViewPager(page))
        }
    }

    open fun initViewPager(viewPager: XViewPager?) {
        this.mViewPager = viewPager
    }

    open fun isVertical() = mViewPager?.isVertical() ?: false
    open fun isHorizontal() = mViewPager?.isHorizontal() ?: false
    open fun isRtl() = mViewPager?.isRtl() ?: false
}