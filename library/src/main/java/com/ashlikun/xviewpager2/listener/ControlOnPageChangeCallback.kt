package com.ashlikun.xviewpager2.listener

import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import com.ashlikun.xviewpager2.adapter.BasePageAdapter
import com.ashlikun.xviewpager2.view.BannerViewPager

/**
 * 作者　　: 李坤
 * 创建时间: 2020/3/17　16:23
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：控制Banner不会滑动到第一个和最后一个
 */
internal class ControlOnPageChangeCallback(var bannerViewPager: BannerViewPager) : ViewPager2.OnPageChangeCallback() {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        if (positionOffset == 0f && bannerViewPager.isCanLoop()) {
            if (position < BasePageAdapter.MULTIPLE_COUNT) {
                //后面的
                val pp = BasePageAdapter.MULTIPLE_COUNT - position - 1
                bannerViewPager.setCurrentItemReal(bannerViewPager.getItemCount() - BasePageAdapter.MULTIPLE_COUNT - 1 - pp, false)
            } else if (position >= bannerViewPager.getItemCount() - BasePageAdapter.MULTIPLE_COUNT) {
                //前面的
                val pp = position - (bannerViewPager.getItemCount() - BasePageAdapter.MULTIPLE_COUNT)
                bannerViewPager.setCurrentItemReal(BasePageAdapter.MULTIPLE_COUNT + pp, false)
            }
        }
    }
}