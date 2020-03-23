package com.ashlikun.xviewpager2.transform

import android.view.View
import androidx.annotation.Px
import androidx.viewpager2.widget.ViewPager2
import com.ashlikun.xviewpager2.view.XViewPager

/**
 * 作者　　: 李坤
 * 创建时间: 2020/3/20　16:20
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：一屏多页的效果
 */
class MarginMultiPageTransformer : BasePageTransformer {
    private var mMarginPx = 0
    private var mLeftOrTopPaddingPx = 0
    private var mRightOrBottomPaddingPx = 0
    private var mMinScale = -1f
    private val scaleIntransformer: ScaleInTransformer by lazy {
        val aa = ScaleInTransformer(mMinScale)
        aa.initViewPager(mViewPager)
        aa
    }

    constructor(@Px marginPx: Int, @Px paddingPx: Int) : this(marginPx, paddingPx, paddingPx)

    constructor(@Px marginPx: Int, @Px leftOrTopPaddingPx: Int, @Px rightOrBottomPaddingPx: Int, minScale: Float = -1f) : super() {
        mMarginPx = marginPx
        mLeftOrTopPaddingPx = leftOrTopPaddingPx
        mRightOrBottomPaddingPx = rightOrBottomPaddingPx
        mMinScale = minScale
    }

    override fun initViewPager(viewPager: XViewPager?) {
        super.initViewPager(viewPager)
        if (isVertical()) {
            mViewPager?.recyclerView?.setPadding(0, mLeftOrTopPaddingPx, 0, mRightOrBottomPaddingPx)
        } else {
            mViewPager?.recyclerView?.setPadding(mLeftOrTopPaddingPx, 0, mRightOrBottomPaddingPx, 0)
        }
        mViewPager?.recyclerView?.clipToPadding = false
    }

    override fun transformPage(page: View, position: Float) {
        super.transformPage(page, position)
        val offset = mMarginPx * position
        if (isVertical()) {
            page.translationY = offset
        } else {
            page.translationX = if (isRtl()) -offset else offset
        }
        if (mMinScale >= 0 && mMinScale < 1) {
            scaleIntransformer.transformPage(page, position)
        }
    }
}