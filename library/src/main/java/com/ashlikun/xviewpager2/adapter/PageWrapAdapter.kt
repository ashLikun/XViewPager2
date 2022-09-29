package com.ashlikun.xviewpager2.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.xviewpager2.ViewPagerUtils

/**
 * @author　　: 李坤
 * 创建时间: 2020/3/13 17:42
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：包裹Adapter
 */
class PageWrapAdapter(var adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        //数据前后各加多少个假数据(前后加上2)，如果循环的时候
        const val MULTIPLE_COUNT = 2
    }

    //是否只有一条数据的时候禁用翻页
    internal var isOneDataOffLoopAndTurning = true
    private var canLoop = true

    private fun isCanLoop() = canLoop && (getRealCount() > if (isOneDataOffLoopAndTurning) 1 else 0)

    override fun getItemCount(): Int {
        if (adapter.itemCount == 0) {
            return 0
        }
        return if (isCanLoop()) getRealCount() + MULTIPLE_COUNT * 2 else getRealCount()
    }

    fun getRealCount(): Int {
        return adapter.itemCount
    }

    fun getRealPosition(position: Int) = if (isCanLoop()) ViewPagerUtils.getRealPosition(position, itemCount) else position

    /**
     * 反转换
     */
    fun getRealFanPosition(position: Int) = if (isCanLoop()) position + MULTIPLE_COUNT else position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapter.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapter.onBindViewHolder(holder, getRealPosition(position))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        adapter.onBindViewHolder(holder, getRealPosition(position), payloads)
    }

    fun setCanLoop(canLoop: Boolean) {
        this.canLoop = canLoop
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        adapter.onAttachedToRecyclerView(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        adapter.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        adapter.onViewAttachedToWindow(holder)
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        super.onViewAttachedToWindow(holder)
        adapter.onViewDetachedFromWindow(holder)
    }

    override fun getItemViewType(position: Int) = adapter.getItemViewType(getRealPosition(position))
    override fun getItemId(position: Int) = adapter.getItemId(getRealPosition(position))
    override fun setHasStableIds(hasStableIds: Boolean) {
        adapter.setHasStableIds(hasStableIds)
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return adapter.onFailedToRecycleView(holder)
    }
}