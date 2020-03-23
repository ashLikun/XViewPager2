package com.ashlikun.xviewpager2.indicator

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ashlikun.xviewpager2.R
import com.ashlikun.xviewpager2.ViewPagerUtils.dip2px
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2020/3/13 13:21
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：Banner指示器的View接口，其他view只需实现这个接口就可以了
 * 继承自LinearLayout
 */
abstract class IBannerIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attrs, defStyleAttr) {
    /**
     * 资源，必须要有大小
     */
    var selectDraw: Drawable?
        protected set
    var noSelectDraw: Drawable?
        protected set

    /**
     * 间距
     */
    @JvmField
    var space = 3

    @JvmField
    protected var datas: List<Any>? = null

    @JvmField
    protected var pointViews: ArrayList<View> = ArrayList()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.IBannerIndicator)
        space = a.getDimension(R.styleable.IBannerIndicator_ind_space, dip2px(context, space.toFloat()).toFloat()).toInt()
        selectDraw = a.getDrawable(R.styleable.IBannerIndicator_ind_select)
        noSelectDraw = a.getDrawable(R.styleable.IBannerIndicator_ind_no_select)
        val indGravity = a.getInt(R.styleable.IBannerIndicator_ind_gravity, Gravity.CENTER)
        if (selectDraw == null) {
            selectDraw = resources.getDrawable(R.drawable.banner_circle_select)
        }
        if (noSelectDraw == null) {
            noSelectDraw = resources.getDrawable(R.drawable.banner_circle_default)
        }
        a.recycle()
        gravity = indGravity
        orientation = HORIZONTAL
        if (selectDraw != null) {
            selectDraw!!.setBounds(0, 0, selectDraw!!.intrinsicWidth, selectDraw!!.intrinsicHeight)
        }
        if (noSelectDraw != null) {
            noSelectDraw!!.setBounds(0, 0, noSelectDraw!!.intrinsicWidth, noSelectDraw!!.intrinsicHeight)
        }
        initView(context, attrs)
    }

    protected abstract fun initView(context: Context?, attrs: AttributeSet?)

    /**
     * 底部指示器资源图片
     *
     * @param
     */
    abstract fun notifyDataSetChanged(selectIndex: Int): IBannerIndicator?

    /**
     * 添加数据
     *
     * @param datas
     */
    open fun setPages(datas: List<Any>, selectIndex: Int): IBannerIndicator? {
        this.datas = datas
        notifyDataSetChanged(selectIndex)
        return this
    }

    open fun setSelectDraw(selectDraw: Drawable?, selectIndex: Int): IBannerIndicator? {
        if (selectDraw != null) {
            this.selectDraw = selectDraw
            notifyDataSetChanged(selectIndex)
        }
        return this
    }

    fun setNoSelectDraw(noSelectDraw: Drawable?, selectIndex: Int): IBannerIndicator {
        if (noSelectDraw != null) {
            this.noSelectDraw = noSelectDraw
            notifyDataSetChanged(selectIndex)
        }
        return this
    }

    @JvmField
    var onPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {}
        val itemCount: Int
            get() = if (datas == null) 0 else datas!!.size

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            if (position >= itemCount) {
                return
            }
            onPointScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            if (datas == null) {
                return
            }
            if (position >= itemCount) {
                return
            }
            onPointSelected(position)
        }
    }

    abstract fun onPointSelected(selectIndex: Int)
    abstract fun onPointScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int)


}