package com.ashlikun.xviewpager2.simple

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ashlikun.glideutils.GlideUtils
import com.ashlikun.xviewpager2.adapter.BasePageAdapter

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
            "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4135477902,3355939884&fm=26&gp=0.jpg",
            "http://img1.cache.netease.com/catchpic/A/A0/A0153E1AEDA115EAE7061A0C7EBB69D2.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1584594344314&di=4eb56ec22f47949d7c36069f55403e5c&imgtype=0&src=http%3A%2F%2Fimg.improve-yourmemory.com%2Fpic%2Fe573f475a02c84bf35a44be7cff56307-0.jpg",
            "http://uploadfile.bizhizu.cn/up/03/50/95/0350955b21a20b6deceea4914b1cfeeb.jpg.source.jpg",
            "http://pic1.win4000.com/wallpaper/7/5860842b353da.jpg",
            "http://pic1.win4000.com/wallpaper/b/566a37b05aac3.jpg")
}


class BannerAdapter1 @JvmOverloads constructor(context: Context, datas: List<String> = BannerAdapter.RESURL) : BasePageAdapter<String>(context, datas) {
    override fun convert(holder: MyViewHolder, data: String) {
        val imageView = holder.itemView as ImageView
        GlideUtils.show(imageView, data)
    }

    override fun createView(context: Context): View {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.layoutParams = ViewGroup.LayoutParams(-1, -1)
        return imageView
    }
}