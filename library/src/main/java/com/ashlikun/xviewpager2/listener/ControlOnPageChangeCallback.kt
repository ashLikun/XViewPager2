package com.ashlikun.xviewpager2.listener

import android.util.Log
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.ScrollState
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
            var position = bannerViewPager.getCurrentItem()
            if (position < BasePageAdapter.MULTIPLE_COUNT) {
                //最后一个
                bannerViewPager.setCurrentItem(bannerViewPager.getItemCount() - BasePageAdapter.MULTIPLE_COUNT - 1, false)
            } else if (position >= bannerViewPager.getItemCount() - BasePageAdapter.MULTIPLE_COUNT) {
                //第一个
                bannerViewPager.setCurrentItem(BasePageAdapter.MULTIPLE_COUNT, false)
            }
        }
    }
}