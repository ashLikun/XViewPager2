package com.ashlikun.xviewpager2.fragment

import android.content.Context
import android.util.AttributeSet
import android.util.LruCache
import android.util.SparseArray
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import com.ashlikun.xviewpager2.R

/**
 * 作者　　: 李坤
 * 创建时间: 2019/4/10　17:02
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：用于fragment一次加载一个的，加载过了不用重新加载
 * 利用hind和show
 * 生命周期和ViewPager使用的一样
 * 必须依赖 [FragmentPagerAdapter]
 */
class FragmentLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    /**
     * 缓存时候的Fragment
     */
    private var mFragments: SparseArray<Fragment?> = SparseArray<Fragment?>()
    var adapter: FragmentPagerAdapter? = null
        private set
    private var currentPosition = 0
    private var lruCache: LruCache<Int, Int>? = null

    /**
     * 最大缓存个数
     */
    protected var maxCache = Int.MAX_VALUE

    init {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FragmentLayout)
        setOffscreenPageLimit(a.getInt(R.styleable.FragmentLayout_fl_maxCache, maxCache))
        a.recycle()
    }

    val itemCount: Int
        get() = mFragments.size()

    /**
     * 设置适配器
     *
     * @param adapter
     */
    fun setAdapter(adapter: FragmentPagerAdapter) {
        //清空已有的
        if (this.adapter != null) {
            for (i in 0 until itemCount) {
                val f = geFragment(i)
                if (f != null) {
                    adapter.removeFragment(adapter.getItemId(i))
                }
            }
            mFragments.clear()
        }
        lruCache = object : LruCache<Int, Int>(maxCache) {
            override fun entryRemoved(evicted: Boolean, key: Int, oldValue: Int?, newValue: Int?) {
                super.entryRemoved(evicted, key, oldValue, newValue)
                if (evicted) {
                    val ff = geFragment(key)
                    if (ff != null) {
                        adapter.removeFragment(adapter.getItemId(key))
                        mFragments.remove(key)
                    }
                }
            }
        }
        this.adapter = adapter
        showFragment(currentPosition)
    }

    /**
     * 显示一个fragment
     *
     * @param position
     */
    private fun showFragment(position: Int) {
        if (adapter == null) return
        var f = geFragment(position)
        val ft: FragmentTransaction = adapter!!.fragmentManager.beginTransaction()
        //隐藏其他的
        for (i in 0 until itemCount) {
            val cacheFragment = geFragment(mFragments.keyAt(i))
            if (cacheFragment != null) {
                ft?.setMaxLifecycle(cacheFragment, Lifecycle.State.STARTED)
                ft.hide(cacheFragment)
            }
        }
        adapter?.bindViewHolder(adapter!!.createViewHolder(this, position), position)
        //是否已经添加了
        if (f != null) {
            ft.setMaxLifecycle(f, Lifecycle.State.RESUMED)
                    .show(f)
                    .commitNow()
        } else {
            if (itemCount > 0) {
                ft.commitNow()
            }
            f = adapter!!.getCacheFragment(position)
            mFragments.put(position, f)
        }
        lruCache!!.put(position, position)
    }

    /**
     * 隐藏一个fragment
     *
     * @param position
     */
    private fun hindFragment(position: Int) {
        val f = geFragment(position)
        if (f != null) {

            //已经添加了
            adapter?.fragmentManager?.beginTransaction()
                    ?.setMaxLifecycle(f, Lifecycle.State.STARTED)
                    ?.hide(f)
                    ?.commitNow()
        }
    }

    /**
     * 设置当前显示的fragment
     *
     * @param position
     */
    var currentItem: Int
        get() = currentPosition
        set(position) {
            if (currentPosition != position) {
                currentPosition = position
                showFragment(currentPosition)
            }
        }

    fun setCurrentPosition(currentPosition: Int) {
        this.currentPosition = currentPosition
    }

    /**
     * 最大缓存个数，在setAdapter之前设置
     *
     * @param limit
     */
    fun setOffscreenPageLimit(limit: Int) {
        var limit = limit
        if (limit <= 0) {
            limit = 1
        }
        maxCache = limit
    }

    /**
     * 获取缓存的fragment
     * 前提是开启缓存
     *
     * @param position
     * @return
     */
    fun geFragment(position: Int): Fragment? {
        return mFragments[position]
    }

}