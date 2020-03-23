package com.ashlikun.xviewpager2.listener

import androidx.annotation.Px
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.ScrollState
import com.ashlikun.xviewpager2.view.BannerViewPager

/**
 * 作者　　: 李坤
 * 创建时间: 2020/3/17　16:23
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：实现翻页时候的真实位置回调给用户
 */
internal class RealOnPageChangeCallback(var callback: OnPageChangeCallback, var bannerViewPager: BannerViewPager) : OnPageChangeCallback() {
    private var mPreviousPosition = -1
    override fun onPageScrolled(position: Int, positionOffset: Float, @Px positionOffsetPixels: Int) {
        if (bannerViewPager.getRealItemCount() <= 0) {
            return
        }
        val realPosition = bannerViewPager.getRealPosition(position)
        callback.onPageScrolled(realPosition,
                positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
        if (bannerViewPager.getRealItemCount() <= 0) {
            return
        }
        val realPosition = bannerViewPager.getRealPosition(position)
        if (mPreviousPosition != realPosition) {
            mPreviousPosition = realPosition
            callback.onPageSelected(realPosition)
        }
    }

    override fun onPageScrollStateChanged(@ScrollState state: Int) {
        if (bannerViewPager.getRealItemCount() <= 0) {
            return
        }
        callback.onPageScrollStateChanged(state)
    }
}