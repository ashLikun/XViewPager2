package com.ashlikun.xviewpager2.adapter

import android.content.Context
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.xviewpager2.ViewPagerUtils
import com.ashlikun.xviewpager2.adapter.BasePageAdapter.MyViewHolder
import com.ashlikun.xviewpager2.listener.OnItemClickListener

/**
 * @author　　: 李坤
 * 创建时间: 2020/3/13 17:42
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：banner的基础适配器
 */
open abstract class BasePageAdapter<T>(var context: Context, data: List<T>? = null) : RecyclerView.Adapter<MyViewHolder>() {
    companion object {
        //数据前后各加多少个假数据(前后加上2)，如果循环的时候
        const val MULTIPLE_COUNT = 2
    }

    //是否只有一条数据的时候禁用翻页
    internal var isOneDataOffLoopAndTurning = true
    private var canLoop = true
    private var onItemClickListener: OnItemClickListener<T>? = null
    private var mDatas: List<T>? = null

    init {
        mDatas = data
    }

    private fun isCanLoop() = canLoop && (getRealCount() > if (isOneDataOffLoopAndTurning) 1 else 0)

    override fun getItemCount(): Int {
        if (mDatas == null) {
            return 0
        }
        return if (isCanLoop()) getRealCount() + MULTIPLE_COUNT * 2 else getRealCount()
    }

    fun getRealCount(): Int {
        return mDatas?.size ?: 0
    }


    fun getRealPosition(position: Int) = if (isCanLoop()) ViewPagerUtils.getRealPosition(position, itemCount) else position

    /**
     * 反转换
     */
    fun getRealFanPosition(position: Int) = if (isCanLoop()) position + MULTIPLE_COUNT else position

    fun getItemData(position: Int): T? {
        if (position >= 0 && position < getRealCount() && mDatas != null) {
            return mDatas!![position]
        }
        return null
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = createView(context)
        if (view.layoutParams == null) {
            view.layoutParams = RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        } else {
            view.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        }
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val realPosition = getRealPosition(position)
        var data: T? = getItemData(realPosition)
        holder.itemView.setOnClickListener(OnPageClickListener(realPosition))
        if (data != null) {
            convert(holder, data)
        }
    }

    fun setCanLoop(canLoop: Boolean) {
        this.canLoop = canLoop
    }

    fun setDatas(datas: List<T>?) {
        this.mDatas = datas
    }

    fun getDatas(): List<T>? = this.mDatas

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        this.onItemClickListener = onItemClickListener
    }

    /**
     * 创建新的view
     */
    abstract fun createView(context: Context): View

    /**
     * 更新
     */
    abstract fun convert(holder: MyViewHolder, data: T)

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        protected val mViews: SparseArray<View> by lazy {
            SparseArray<View>()
        }

        /**
         * 通过viewId获取控件
         *
         * @param viewId
         * @return
         */
        fun <T : View> getView(viewId: Int): T? {
            var view = mViews[viewId]
            if (view == null) {
                view = itemView.findViewById(viewId)
                mViews.put(viewId, view)
            }
            return view as T?
        }

        fun getImageView(viewId: Int): ImageView? = getView(viewId)
    }

    internal inner class OnPageClickListener(var position: Int) : View.OnClickListener {
        override fun onClick(v: View) {
            getItemData(position)?.run {
                onItemClickListener?.onItemClick(this, position)
            }
        }
    }
}