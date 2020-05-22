package com.ashlikun.xviewpager2.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Path
import android.graphics.RectF
import android.util.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewParent
import android.widget.FrameLayout
import androidx.annotation.Px
import androidx.core.util.set
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OffscreenPageLimit
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ashlikun.xviewpager2.R
import com.ashlikun.xviewpager2.ViewPagerUtils
import com.ashlikun.xviewpager2.transform.BasePageTransformer
import com.ashlikun.xviewpager2.transform.CompositePageTransformer
import java.util.ArrayList
import kotlin.math.abs

/**
 * 作者　　: 李坤
 * 创建时间:2017/8/24 0024　23:55
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：ViewPager嵌套滑动处理
 * 1：对于指定的控件嵌套滑动,百度地图，高德地图，RecyclerView
 * 2:ViewPager是否可以左右滑动[.setUserInputEnabled]
 * 3:外层是下拉刷新控件可以处理嵌套滑动问题
 */
open class XViewPager
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        FrameLayout(context, attrs, defStyleAttr) {

    private var startX = 0f
    private var startY = 0f
    private var refreshLayout: View? = null
    private var isRefreshLayoutSet = false

    //其他的XViewPager 是否被设置过
    private var isOtherXViewPagerSet: SparseBooleanArray? = null
    private var isOtherXViewPager: ArrayList<XViewPager>? = null
    private var touchSlop = 0

    //缩放比例
    var ratio: Float = 0f
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    //按照那个值为基础 0:宽度 1：高度
    var ratioOrientation = 0
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    //用于裁剪画布的路径
    protected val clipPath: Path by lazy {
        Path()
    }

    //圆角半径  左上
    var radiusLeftTop = -1f
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    //圆角半径  右上
    var radiusRightTop = -1f
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    //圆角半径  右下
    var radiusRightBottom = -1f
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    //圆角半径  坐下
    var radiusLeftBottom = -1f
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    //ViewPager2
    val viewPager: ViewPager2 by lazy {
        ViewPager2(context, attrs)
    }

    //RecyclerView
    val recyclerView: RecyclerView by lazy {
        try {
            viewPager.getChildAt(0) as RecyclerView
        } catch (e: Exception) {
            e.printStackTrace()
            //防止异常
            RecyclerView(context)
        }
    }

    //设置多个PageTransformer
    val compositePageTransformer: CompositePageTransformer by lazy {
        val aa = CompositePageTransformer()
        viewPager.setPageTransformer(aa)
        aa
    }

    /**
     * 页面切换速度,值越大滑动越慢，滑动太快会使3d效果不明显 Ms
     * 只有在主动调用setCurrentItem生效
     */
    var scrollDuration = -1


    init {
        //添加ViewPager2
        addView(viewPager)
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        touchSlop = ViewConfiguration.get(getContext()).scaledTouchSlop
        val a = context.obtainStyledAttributes(attrs, R.styleable.XViewPager)
        ratio = a.getFloat(R.styleable.XViewPager_xvp_ratio, 0f)
        ratioOrientation = a.getInt(R.styleable.XViewPager_xvp_ratio_orientation, ratioOrientation)
        isUserInputEnabled = a.getBoolean(R.styleable.XViewPager_xvp_isCanTouchScroll, true)
        scrollDuration = a.getInt(R.styleable.XViewPager_xvp_scrollDuration, scrollDuration)
        //圆角设置
        if (a.hasValue(R.styleable.XViewPager_xvp_radius)) {
            radiusLeftBottom = a.getDimension(R.styleable.XViewPager_xvp_radius, -1f)
            radiusRightBottom = radiusLeftBottom
            radiusRightTop = radiusRightBottom
            radiusLeftTop = radiusRightTop
        }
        if (a.hasValue(R.styleable.XViewPager_xvp_radiusLeftTop)) {
            radiusLeftTop = a.getDimension(R.styleable.XViewPager_xvp_radiusLeftTop, radiusLeftTop)
        }
        if (a.hasValue(R.styleable.XViewPager_xvp_radiusRightTop)) {
            radiusRightTop = a.getDimension(R.styleable.XViewPager_xvp_radiusRightTop, radiusRightTop)
        }
        if (a.hasValue(R.styleable.XViewPager_xvp_radiusRightBottom)) {
            radiusRightBottom = a.getDimension(R.styleable.XViewPager_xvp_radiusRightBottom, radiusRightBottom)
        }
        if (a.hasValue(R.styleable.XViewPager_xvp_radiusLeftBottom)) {
            radiusLeftBottom = a.getDimension(R.styleable.XViewPager_xvp_radiusLeftBottom, radiusLeftBottom)
        }
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        if (ratio > 0) {
            if (ratioOrientation == 0) {
                //宽度不变
                heightSize = (widthSize / ratio).toInt()
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,
                        MeasureSpec.EXACTLY)
            } else {
                //高度不变
                widthSize = (heightSize / ratio).toInt()
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,
                        MeasureSpec.EXACTLY)
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return viewPager!!.canScrollVertically(direction)
    }

    override fun canScrollHorizontally(direction: Int): Boolean {
        return viewPager!!.canScrollHorizontally(direction)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // 记录手指按下的位置
                startY = ev.y
                startX = ev.x
                requestDisallowInterceptTouchEventmy(true)
                initOtherXViewPager()
            }
            MotionEvent.ACTION_MOVE -> {
                // 获取当前手指位置
                val endY = ev.y
                val endX = ev.x
                val distanceX = abs(endX - startX)
                val distanceY = abs(endY - startY)
                //这里只处理水平的
                if (isHorizontal()) {
                    if (distanceX > touchSlop && distanceX > distanceY) {
                        val or = (startX - endX).toInt()
                        val canScrollHorizontally = canScrollHorizontally(or)
                        if (!canScrollHorizontally) {
                            //设置父控件可以滚动
                            setViewPagerUserInputEnabled(true)
                        }
                        requestDisallowInterceptTouchEventmy(canScrollHorizontally)

                        setRefreshEnable(false)
                    } else if (distanceY > touchSlop && distanceX < distanceY) {
                        //垂直滑动，主动释放
                        requestDisallowInterceptTouchEventmy(false)
                        //设置父控件不可以滚动
                        setViewPagerUserInputEnabled(false)
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> if (isHorizontal()) {
                requestDisallowInterceptTouchEventmy(false)
                setRefreshEnable(true)
                //设置父控件可以滚动
                setViewPagerUserInputEnabled(true)
                cleanOtherXViewPager()
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun requestDisallowInterceptTouchEventmy(disallowIntercept: Boolean) {
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept)
        }

    }


    private fun setRefreshEnable(isEnabled: Boolean) {
        if (refreshLayout != null) {
            if (!isEnabled) {
                if (refreshLayout!!.isEnabled) {
                    refreshLayout?.isEnabled = isEnabled
                    isRefreshLayoutSet = true
                }
            } else {
                if (isRefreshLayoutSet) {
                    refreshLayout?.isEnabled = true
                    isRefreshLayoutSet = false
                }
            }
        }
    }

    private fun cleanOtherXViewPager() {
        isOtherXViewPager?.clear()
    }

    private fun initOtherXViewPager() {
        var pp: ViewParent? = parent

        while (pp != null) {
            if (pp is XViewPager) {
                if (isOtherXViewPager == null) {
                    isOtherXViewPager = arrayListOf()
                }
                if (isOtherXViewPagerSet == null) {
                    isOtherXViewPagerSet = SparseBooleanArray()
                }
                isOtherXViewPager!!.add(pp)
            }
            pp = pp?.parent
        }
    }

    private fun setViewPagerUserInputEnabled(isEnabled: Boolean) {
        isOtherXViewPager?.forEach { pp ->
            if (!isEnabled) {
                if (pp.isEnabled) {
                    pp.isUserInputEnabled = false
                    isOtherXViewPagerSet!!.put(pp.hashCode(), true)
                }
            } else {
                if (isOtherXViewPagerSet!!.get(pp.hashCode())) {
                    pp.isUserInputEnabled = true
                    isOtherXViewPagerSet!!.put(pp.hashCode(), false)
                }
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        if (radiusLeftTop != -1f || radiusRightTop != -1f || radiusRightBottom != -1f || radiusLeftBottom != -1f) {
            clipPath.reset()
            val radii = floatArrayOf(radiusLeftTop, radiusLeftTop, radiusRightTop, radiusRightTop, radiusRightBottom, radiusRightBottom, radiusLeftBottom, radiusLeftBottom)
            clipPath.addRoundRect(RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat()), radii, Path.Direction.CW)
            //viewPage内部是滚动实现，这里要加上偏移量
            val matrix = Matrix()
            matrix.setTranslate(scrollX.toFloat(), scaleY)
            clipPath.transform(matrix)
            canvas.save()
            canvas.clipPath(clipPath)
            super.dispatchDraw(canvas)
            canvas.restore()
        } else {
            super.dispatchDraw(canvas)
        }
    }


    /**
     * 设置下拉刷新控件
     * 在滑动的时候会判断是否禁用下拉刷新
     * 这里如果 下拉控件子控件满足isNestedScrollingEnabled  就不用调用这个方法，内部自己处理
     *
     * @param refreshLayout
     */
    fun setRefreshLayout(refreshLayout: View?) {
        this.refreshLayout = refreshLayout
    }


    /**
     * 4个方向的圆角
     */
    fun setRadius(radius: Float) {
        radiusLeftTop = radius
        radiusRightTop = radius
        radiusRightBottom = radius
        radiusLeftBottom = radius
        invalidate()
    }

    /**
     * 4个方向的圆角
     */
    fun setRadius(radiusLeftTop: Float = this.radiusLeftTop,
                  radiusRightTop: Float = this.radiusRightTop,
                  radiusRightBottom: Float = this.radiusRightBottom,
                  radiusLeftBottom: Float = this.radiusLeftBottom) {
        this.radiusLeftTop = radiusLeftTop
        this.radiusRightTop = radiusRightTop
        this.radiusRightBottom = radiusRightBottom
        this.radiusLeftBottom = radiusLeftBottom
        invalidate()
    }


    /********************************************************************************************
     *                            ViewPager本身的主要使用方法
     *******************************************************************************************/
    open fun getAdapter() = viewPager.adapter

    open fun setAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>) {
        viewPager.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    open fun setOrientation(@ViewPager2.Orientation orientation: Int) {
        viewPager.orientation = orientation
    }

    @ViewPager2.Orientation
    fun getOrientation(): Int {
        return viewPager.orientation
    }

    fun isVertical() = getOrientation() == ViewPager2.ORIENTATION_VERTICAL
    fun isHorizontal() = getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL

    /**
     * 是否是从右到左的布局
     */
    fun isRtl() = recyclerView.layoutManager?.layoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL

    /**
     * 扩展这个方法使其具有滚动速度
     */
    @JvmOverloads
    open fun setCurrentItem(item: Int, smoothScroll: Boolean = true) {
        if (scrollDuration > 0 && smoothScroll) {
            if (getAdapter()?.itemCount ?: 0 <= 0) {
                return
            }
            var item = item.coerceAtLeast(0).coerceAtMost(getAdapter()!!.itemCount - 1)
            if (item == getCurrentItem()) {
                return
            }

            try {
                var previousItem = getCurrentItem()
                //源码反射
                ViewPagerUtils.setField(viewPager, "mCurrentItem", item)
                var isIdle: Boolean
                var mScrollEventAdapter = ViewPagerUtils.getField(viewPager, "mScrollEventAdapter")
                isIdle = ViewPagerUtils.getMethod(mScrollEventAdapter, "isIdle") as Boolean? ?: true
                ViewPagerUtils.getField(viewPager, "mAccessibilityProvider")?.run {
                    ViewPagerUtils.getMethod(this, "onSetNewCurrentItem")
                }
                if (!isIdle) {
                    previousItem = (ViewPagerUtils.getMethod(mScrollEventAdapter, "getRelativeScrollPosition") as Double?)?.toInt()
                            ?: previousItem
                }
                ViewPagerUtils.getMethod(mScrollEventAdapter, "notifyProgrammaticScroll", item, smoothScroll)
                //源码反射结束

                // 为了平滑滚动，跳远到附近的项目。
                if (abs(item - previousItem) > 3) {
                    recyclerView.scrollToPosition(if (item > previousItem) item - 3 else item + 3)
                    recyclerView.post(SmoothScrollToPosition(item, scrollDuration, recyclerView))
                } else {
                    SmoothScrollToPosition(item, scrollDuration, recyclerView).run()
                }
            } catch (e: Exception) {
                //使用默认的
                viewPager.setCurrentItem(item, smoothScroll)
            }
        } else {
            //使用默认的
            viewPager.setCurrentItem(item, smoothScroll)
        }
    }


    // 异步执行
    internal class SmoothScrollToPosition
    internal constructor(private val mPosition: Int, private val mScrollDuration: Int, private val mRecyclerView: RecyclerView)
        : Runnable {
        override fun run() {
            val linearSmoothScroller = object : LinearSmoothScroller(mRecyclerView.context) {
                override fun calculateTimeForScrolling(dx: Int): Int {
                    return mScrollDuration / 2
                }
            }
            linearSmoothScroller.targetPosition = mPosition
            mRecyclerView.layoutManager?.startSmoothScroll(linearSmoothScroller)
        }
    }

    open fun getCurrentItem() = viewPager.currentItem

    open fun getScrollState() = viewPager.scrollState

    open fun beginFakeDrag() = viewPager.beginFakeDrag()

    open fun fakeDragBy(@Px offsetPxFloat: Float): Boolean {
        return viewPager.fakeDragBy(offsetPxFloat)
    }

    open fun endFakeDrag() = viewPager.endFakeDrag()


    fun isFakeDragging() = viewPager.isFakeDragging

    open fun setOffscreenPageLimit(@OffscreenPageLimit limit: Int) {
        viewPager.offscreenPageLimit = limit
    }

    open fun getOffscreenPageLimit() = viewPager.offscreenPageLimit

    /**
     * ViewPager是否可以滑动
     */
    open var isUserInputEnabled: Boolean
        set(value) {
            viewPager.isUserInputEnabled = value
        }
        get() = viewPager.isUserInputEnabled


    open fun registerOnPageChangeCallback(callback: OnPageChangeCallback) {
        viewPager.registerOnPageChangeCallback(callback)
    }

    open fun unregisterOnPageChangeCallback(callback: OnPageChangeCallback) {
        viewPager.unregisterOnPageChangeCallback(callback)
    }

    /**
     * 添加PageTransformer
     */
    open fun setPageTransformer(transformer: ViewPager2.PageTransformer) {
        addTransformer(transformer)
    }

    /**
     * 添加PageTransformer
     */
    open fun addTransformer(transformer: ViewPager2.PageTransformer) {
        if (transformer is BasePageTransformer) {
            transformer.initViewPager(this)
        }
        compositePageTransformer.addTransformer(transformer)
    }

    /**
     * 移除PageTransformer
     */
    open fun removeTransformer(transformer: ViewPager2.PageTransformer) {
        compositePageTransformer.removeTransformer(transformer)
    }

    open fun requestTransform() {
        viewPager.requestTransform()
    }

    open fun addItemDecoration(decor: ItemDecoration, index: Int = -1) {
        viewPager.addItemDecoration(decor, index)
    }

    open fun getItemDecorationAt(index: Int): ItemDecoration {
        return viewPager.getItemDecorationAt(index)
    }

    open fun getItemDecorationCount(): Int {
        return viewPager.itemDecorationCount
    }

    open fun invalidateItemDecorations() {
        viewPager.invalidateItemDecorations()
    }

    open fun removeItemDecorationAt(index: Int) {
        viewPager.removeItemDecorationAt(index)
    }

    open fun removeItemDecoration(decor: ItemDecoration) {
        viewPager.removeItemDecoration(decor)
    }
}