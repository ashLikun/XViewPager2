package com.ashlikun.xviewpager2.simple

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.CommonAdapter

/**
 * 作者　　: 李坤
 * 创建时间: 2020/5/22　9:48
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
class MyListAdapter(context: Context) : CommonAdapter<String>(context, R.layout.item_view, arrayListOf("","","","","","","","","","","","","","","","","","","","","","","","","","","","")) {
    override fun convert(holder: ViewHolder?, t: String?) {

    }

}