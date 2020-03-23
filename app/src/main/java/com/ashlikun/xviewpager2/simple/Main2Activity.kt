package com.ashlikun.xviewpager2.simple

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.xviewpager2.FragmentUtils
import com.ashlikun.xviewpager2.fragment.FragmentPagerAdapter
import com.ashlikun.xviewpager2.fragment.FragmentPagerAdapter.Builder.Companion.create
import kotlinx.android.synthetic.main.activity_main2.*

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/22 0022　下午 3:06
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class Main2Activity : AppCompatActivity() {
    val adapter: FragmentPagerAdapter by lazy {
        FragmentPagerAdapter.Builder.create(this)
                .addItem("/Fragment/test").setId("1").ok()
                .addItem("/Fragment/test").setId("2").ok()
                .addItem("/Fragment/test").setId("3").ok()
                .addItem("/Fragment/test").setId("4").ok()
                .addItem("/Fragment/test").setId("5").ok()
                .addItem("/Fragment/test").setId("6").ok()
                .addItem("/Fragment/test").setId("7").ok()
                .setCache(true)
                .build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        FragmentUtils.removeAll(supportFragmentManager)
        swipeRefresh.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefresh.isRefreshing = false
            }, 1000)
        }
        fragmentLayout.setOffscreenPageLimit(2)
        fragmentLayout.setAdapter(adapter)
        fragmentLayout.setRefreshLayout(swipeRefresh)
    }

    fun onClick1(view: View?) {
        fragmentLayout.viewPager!!.currentItem = 0
    }

    fun onClick2(view: View?) {
        fragmentLayout.viewPager!!.currentItem = 1
    }

    fun onClick3(view: View?) {
        fragmentLayout.viewPager!!.currentItem = 2
    }

    fun onClick4(view: View?) {
        fragmentLayout.viewPager!!.currentItem = 3
    }

    fun onClick5(view: View?) {
        fragmentLayout.viewPager!!.currentItem = 4
    }

    fun onClick6(view: View?) {
        fragmentLayout.viewPager!!.currentItem = 5
    }

    fun onClick7(view: View?) {
        fragmentLayout.viewPager!!.currentItem = 6
    }
}