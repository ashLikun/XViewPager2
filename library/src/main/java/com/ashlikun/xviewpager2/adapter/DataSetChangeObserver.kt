package com.ashlikun.xviewpager2.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 * 作者　　: 李坤
 * 创建时间: 2020/3/13　11:05
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
open abstract class DataSetChangeObserver : RecyclerView.AdapterDataObserver() {
    abstract override fun onChanged()

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        onChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int,
                                    payload: Any?) {
        onChanged()
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        onChanged()
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        onChanged()
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        onChanged()
    }
}