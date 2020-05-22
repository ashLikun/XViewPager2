package com.ashlikun.xviewpager2.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.ashlikun.xviewpager2.R
import com.ashlikun.xviewpager2.ViewPagerUtils
import com.ashlikun.xviewpager2.adapter.BasePageAdapter
import com.ashlikun.xviewpager2.listener.ControlOnPageChangeCallback
import com.ashlikun.xviewpager2.listener.OnItemClickListener
import com.ashlikun.xviewpager2.listener.RealOnPageChangeCallback
import com.ashlikun.xviewpager2.transform.DepthPageTransformer
import java.lang.ref.WeakReference

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/2 17:20
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：一个普通的没有指示器的banner,
 * 1:可以用作广告栏
 * 2:可以用作启动页
 */
open class BannerViewPager
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : XViewPager(context, attrs, defStyleAttr), LifecycleObserver {
    companion object {
        const val DEFAULT_TURNING_TIME: Long = 5000
    }


    //是否可以循环
    private var canLoop = true

    //自动轮播的间隔
    private var turningTime = DEFAULT_TURNING_TIME

    //是否可以自动滚动,外部使用
    private var isAutoTurning = true

    /***
     * 是否开启了翻页
     * @return
     */
    //是否可以自动滚动,内部标识
    var isTurning = false
        private set

    //是否只有一条数据的时候禁用翻页
    private var isOneDataOffLoopAndTurning = true
    private var lifecycle: Lifecycle? = null

    //是否可以自动滚动，内部用于判断触摸屏幕，与view进入焦点
    private var isNeibuAutoTurning = false
    private val adSwitchTask: AdSwitchTask by lazy {
        AdSwitchTask(this)
    }

    init {
        setOffscreenPageLimit(1)
        super.registerOnPageChangeCallback(ControlOnPageChangeCallback(this))
        val a = context.obtainStyledAttributes(attrs, R.styleable.BannerViewPager)
        canLoop = a.getBoolean(R.styleable.BannerViewPager_banner_canLoop, canLoop)
        isOneDataOffLoopAndTurning = a.getBoolean(R.styleable.BannerViewPager_banner_isOneDataOffLoopAndTurning, isOneDataOffLoopAndTurning)
        turningTime = a.getInteger(R.styleable.BannerViewPager_banner_turningTime, turningTime.toInt()).toLong()
        isAutoTurning = a.getBoolean(R.styleable.BannerViewPager_banner_isAutoTurning, isAutoTurning)
        a.recycle()
        //设置滚动的默认时间
        if (scrollDuration <= 0) {
            scrollDuration = 1000
        }
        //只能识别Activity,Fragment无法识别
        setLifecycle(ViewPagerUtils.getLifecycle(getContext()))
        setTurningTime(turningTime)
    }

    fun setTurningTime(turningTime: Long) {
        this.turningTime = turningTime
        if (turningTime > 0) {
            isTurning = true
            isNeibuAutoTurning = true
        }
    }


    fun getLastItem(): Int {
        return (getPagerAdapter()?.getRealCount() ?: 1) - 1
    }

    /**
     * 触碰控件的时候，翻页应该停止，离开的时候如果之前是开启了翻页的话则重新启动翻页
     *
     * @param ev
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isUserInputEnabled && isAutoTurning) {
            val action = ev.action
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_OUTSIDE) {
                // 开始翻页
                if (isNeibuAutoTurning) {
                    startTurning(turningTime)
                }
            } else if (action == MotionEvent.ACTION_DOWN) {
                // 停止翻页
                if (isNeibuAutoTurning) {
                    stopTurning()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    /**
     * 获取实际的位置(对应数据的位置)
     */
    @JvmOverloads
    open fun getRealPosition(position: Int = getCurrentItem()) = getPagerAdapter()?.getRealPosition(position)
            ?: 0


    /**
     * 获取真实的当前位置，对应数据position
     * getCurrentItem 可能超过数据总和
     */
    fun getCurrentItemReal() = getRealPosition()

    /**
     * adapter真实的个数
     *
     * @return
     */
    open fun getRealItemCount() = getPagerAdapter()?.getRealCount() ?: 0

    /**
     * adapter的最大item个数，是假的
     *
     * @return
     */
    open fun getItemCount() = getPagerAdapter()?.itemCount ?: 0

    /**
     * 还原原始的position翻页监听
     * [OnBannerPageChangeListener]
     *
     * @param listener
     */
    override fun registerOnPageChangeCallback(callback: OnPageChangeCallback) {
        super.registerOnPageChangeCallback(RealOnPageChangeCallback(callback, this))
    }


    open fun isCanLoop(): Boolean {
        return canLoop
    }

    open fun setCanLoop(canLoop: Boolean) {
        this.canLoop = canLoop
        if (!canLoop) {
            setCurrentItem(getRealItemCount(), false)
        }
        getPagerAdapter()?.setCanLoop(canLoop)
        getPagerAdapter()?.notifyDataSetChanged()
    }

    /**
     * 手动停止
     */
    open fun stopTurning(): BannerViewPager {
        isTurning = false
        removeCallbacks(adSwitchTask)
        return this
    }

    /**
     * 手动开始
     */
    open fun startTurning(): BannerViewPager {
        return if (turningTime > 0) {
            startTurning(turningTime)
        } else this
    }

    /**
     * 手动开始
     */
    open fun startTurning(turningTime: Long): BannerViewPager {
        if (!isAutoTurning || turningTime <= 0) {
            return this
        }
        //如果是正在翻页的话先停掉
        if (isTurning) {
            stopTurning()
        }
        //设置可以翻页并开启翻页
        isNeibuAutoTurning = true
        this.turningTime = turningTime
        isTurning = true
        if (getRealItemCount() > 0 && !isOneDataOffLoopAndTurning()) {
            postDelayed(adSwitchTask, turningTime)
        }
        return this
    }

    open fun notifyDataSetChanged() = getPagerAdapter()?.notifyDataSetChanged()

    fun getPagerAdapter() = super.getAdapter() as BasePageAdapter<Any>?

    fun getDatas() = getPagerAdapter()?.getDatas()

    /**
     * 设置banner的数据
     */
    open fun setAdapter(adapter: BasePageAdapter<*>) {
        adapter.setCanLoop(canLoop)
        adapter.isOneDataOffLoopAndTurning = isOneDataOffLoopAndTurning
        super.setAdapter(adapter)
        setCurrentItem(0, false)
        if (isTurning) {
            startTurning()
        }
    }


    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(getPagerAdapter()?.getRealFanPosition(item) ?: item, smoothScroll)
    }

    /**
     * 转换成真实的
     */
    fun setCurrentItemReal(item: Int, smoothScroll: Boolean = true) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isCanLoop() && isNeibuAutoTurning) {
            startTurning()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (isCanLoop() && isNeibuAutoTurning) {
            stopTurning()
        }
    }

    /**
     * 是否只有一条数据的时候禁用自动翻页
     *
     * @return
     */
    fun isOneDataOffLoopAndTurning(): Boolean {
        return isOneDataOffLoopAndTurning && getPagerAdapter()?.getRealCount() ?: 0 <= 1
    }

    /**
     * 设置是否只有一条数据的时候禁用翻页
     *
     * @param oneDataOffLoopAndTurning
     */
    fun setOneDataOffLoopAndTurning(oneDataOffLoopAndTurning: Boolean) {
        isOneDataOffLoopAndTurning = oneDataOffLoopAndTurning
        getPagerAdapter()?.isOneDataOffLoopAndTurning = isOneDataOffLoopAndTurning
    }

    /**
     * 设置自动滚动
     *
     * @param isAutoTurning
     */
    fun setAutoTurning(isAutoTurning: Boolean) {
        this.isAutoTurning = isAutoTurning
        startTurning()
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<*>) {
        getPagerAdapter()?.setOnItemClickListener(onItemClickListener as OnItemClickListener<Any>)
    }

    /**
     * 实现生命周期绑定,null就是清空
     */
    fun setLifecycle(lifecycle: Lifecycle?) {
        this.lifecycle?.removeObserver(this)
        this.lifecycle = lifecycle
        lifecycle?.addObserver(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
        startTurning()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
        stopTurning()
    }


    /**
     * 自动滚动的倒计时
     */
    internal class AdSwitchTask(bannerViewPager: BannerViewPager) : Runnable {
        private val reference: WeakReference<BannerViewPager?> = WeakReference<BannerViewPager?>(bannerViewPager)
        override fun run() {
            val bannerViewPager = reference.get()
            if (bannerViewPager != null && bannerViewPager.isTurning) {
                var page = bannerViewPager.getCurrentItem() + 1
                bannerViewPager.setCurrentItemReal(page)
                //不循环的时候 如果是最后一个就判断是否继续
                if (!bannerViewPager.isCanLoop()) {
                    if (page >= bannerViewPager.getItemCount() - BasePageAdapter.MULTIPLE_COUNT - 1) {
                        bannerViewPager.postDelayed(bannerViewPager.adSwitchTask, bannerViewPager.turningTime)
                    }
                } else {
                    bannerViewPager.postDelayed(bannerViewPager.adSwitchTask, bannerViewPager.turningTime)
                }
            }
        }
    }
}