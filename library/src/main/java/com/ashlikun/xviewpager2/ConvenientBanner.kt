package com.ashlikun.xviewpager2

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.xviewpager2.ViewPagerUtils.dip2px
import com.ashlikun.xviewpager2.adapter.DataSetChangeObserver
import com.ashlikun.xviewpager2.indicator.DefaultIndicator
import com.ashlikun.xviewpager2.indicator.IBannerIndicator
import com.ashlikun.xviewpager2.indicator.TransIndicator
import com.ashlikun.xviewpager2.indicator.ZoomIndicator
import com.ashlikun.xviewpager2.view.BannerViewPager

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/2 17:20
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：封装带有指示器的banner
 */
class ConvenientBanner
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BannerViewPager(context, attrs, defStyleAttr) {
    var indicator: IBannerIndicator? = null
        private set

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ConvenientBanner)
        indicator = when (a.getInt(R.styleable.ConvenientBanner_ind_style, 1)) {
            1 -> DefaultIndicator(context, attrs)
            2 -> ZoomIndicator(context, attrs)
            3 -> TransIndicator(context, attrs)
            else -> DefaultIndicator(context, attrs)
        }
        a.recycle()
        addIndicatorView()
    }

    private fun addIndicatorView() {
        if (indicator?.layoutParams == null) {
            val params2 = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            val dp10 = dip2px(context, 10f)
            params2.setMargins(dp10, dp10, dp10, dp10)
            //底部居中
            params2.gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
            indicator?.layoutParams = params2
        }
        if (indicator != null) {
            addView(indicator)
            registerOnPageChangeCallback(indicator!!.onPageChangeCallback)
        }
    }

    /**
     * 设置Indicator
     *
     * @param ind
     */
    fun setIndicator(ind: IBannerIndicator) {
        if (indicator != null) {
            removeView(indicator)
            ind.space = indicator!!.space
            ind.setSelectDraw(indicator!!.selectDraw, getCurrentItemReal())
            ind.setNoSelectDraw(indicator!!.noSelectDraw, getCurrentItemReal())
        }
        indicator = ind
        addIndicatorView()
    }

    private val dataSetChangeObserver: DataSetChangeObserver = object : DataSetChangeObserver() {
        override fun onChanged() {
            indicator?.setPages(getRealItemCount(), getCurrentItemReal())
        }
    }

    /**
     * 设置banner的数据
     */
    override var adapter: RecyclerView.Adapter<*>?
        get() = super.adapter
        set(value) {
            value?.registerAdapterDataObserver(dataSetChangeObserver)
            super.adapter = value
        }

    /**
     * 设置底部指示器是否可见
     *
     * @param visible
     */
    fun setIndicatorViewVisible(visible: Boolean): ConvenientBanner {
        indicator?.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    /**
     * 指示器的方向
     *
     * @return
     */
    fun setIndicatorGravity(gravity: Int): ConvenientBanner {
        indicator?.gravity = Gravity.CENTER_VERTICAL or gravity
        return this
    }

    /***
     * 开始翻页
     *
     * @param autoTurningTime 自动翻页时间
     * @return
     */
    override fun startTurning(autoTurningTime: Long): ConvenientBanner {
        super.startTurning(autoTurningTime)
        return this
    }


}