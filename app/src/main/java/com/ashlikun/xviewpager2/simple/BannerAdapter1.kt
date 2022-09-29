package com.ashlikun.xviewpager2.simple

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.glideutils.GlideUtils
import com.ashlikun.xviewpager2.adapter.PageWrapAdapter

/**
 * 作者　　: 李坤
 * 创建时间: 2020/3/17　13:29
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
object BannerAdapter {
    val RESURL = listOf(
        "http://img.mukewang.com/54bf7e1f000109c506000338-590-330.jpg",
        "http://upload.techweb.com.cn/2015/0114/1421211858103.jpg",
        "http://img1.cache.netease.com/catchpic/A/A0/A0153E1AEDA115EAE7061A0C7EBB69D2.jpg",
        "http://image.tianjimedia.com/uploadImages/2015/202/27/57RF8ZHG8A4T_5020a2a4697650b89" +
                "c394237ba9ffbb45fe8555a2cbec-6O6nmI_fw658.jpg")
    val RESURL2 = listOf(
        "https://img1.baidu.com/it/u=4021455104,3949361732&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500",
        "http://img1.cache.netease.com/catchpic/A/A0/A0153E1AEDA115EAE7061A0C7EBB69D2.jpg",
        "https://img2.baidu.com/it/u=4079772737,3699929909&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500",
        "http://uploadfile.bizhizu.cn/up/03/50/95/0350955b21a20b6deceea4914b1cfeeb.jpg.source.jpg",
        "https://img2.baidu.com/it/u=953139658,3460758390&fm=253&fmt=auto&app=138&f=JPEG?w=889&h=500",
        "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg2.niutuku.com%2Fdesk%2F1208%2F1515%2Fntk-1515-42349.jpg&refer=http%3A%2F%2Fimg2.niutuku.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=auto?sec=1667026521&t=c61500aacf217801464e7fc3b8b8fab5")
    val RESURL3 = listOf(
        "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4135477902,3355939884&fm=26&gp=0.jpg")
}

