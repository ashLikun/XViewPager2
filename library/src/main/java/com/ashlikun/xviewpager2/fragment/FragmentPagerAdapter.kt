package com.ashlikun.xviewpager2.fragment

import android.text.TextUtils
import androidx.collection.LongSparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter2
import androidx.viewpager2.adapter.FragmentViewHolder
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2020/3/13 10:18
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：viewpager显示fragment的适配器,用路由模式去寻找fragment
 * 可以设置缓存fragment，第一次会使用ARouter去发现Fragment，后续就会缓存起来
 * 缓存起来的Fragment不会在ViewPager2里面，但是是存在在FragmentManage里面的
 * 每次更新数据(AdapterDataObserver 任意方法)的时候会清空
 */

class FragmentPagerAdapter private constructor(builder: Builder)
    : FragmentStateAdapter2(builder.fm, builder.mLifecycle) {
    companion object {
        /**
         * 传递给fragment的参数
         */
        const val POSITION = "fpa_POSITION"
    }

    private var pagerItems: List<FragmentPagerItem>?

    /**
     * 是否缓存Fragment
     */
    private var isCache = false

    val fragmentManager: FragmentManager

    /**
     * 缓存时候的Fragment,适配器刷新的时候会清除,只有缓存开启的时候才会创建
     */
    var mCacheFragment: LongSparseArray<Fragment?>? = null
        private set

    //当前的位置
    var mCurrentPosition = 0
    val mCurrentItemDataSetChangeObserver: RecyclerView.AdapterDataObserver by lazy {
        object : com.ashlikun.xviewpager2.adapter.DataSetChangeObserver() {
            override fun onChanged() {
                clearCache()
            }
        }
    }

    init {
        fragmentManager = builder.fm
        pagerItems = builder.items
        setCache(builder.isUseCache)
        if (isCache) {
            //缓存开启的时候，监听数据刷新，去除全部缓存
            registerAdapterDataObserver(mCurrentItemDataSetChangeObserver)
        }
    }

    fun getPagerItems(): List<FragmentPagerItem> {
        if (pagerItems == null) {
            pagerItems = ArrayList()
        }
        return pagerItems!!
    }

    fun getPagerItem(position: Int): FragmentPagerItem {
        return getPagerItems()!!.getOrElse(position) { FragmentPagerItem.get("XViewPager_error") }
    }

    fun setCache(cache: Boolean) {
        isCache = cache
        if (isCache) {
            mCacheFragment = LongSparseArray()
        }
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        mCurrentPosition = position
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        if (isCache) {
            fragment = getCacheFragment<Fragment>(position)
        }
        if (fragment == null) {
            val item = pagerItems!![position]
            //添加一个告诉fragment当前是第几页
            item.addParam(POSITION, position)
            fragment = ARouter.getInstance()
                    .build(item.path)
                    .with(item.param)
                    .navigation() as Fragment
        }
        if (isCache) {
            mCacheFragment!!.put(getItemId(position), fragment)
        }
        return fragment
    }

    override fun removeFragment(itemId: Long) {
        super.removeFragment(itemId, !isCache)
    }

    /**
     * 查找这个id对应 的position
     *
     * @param id
     * @return
     */
    fun findIdPosition(id: String?): Int {
        try {
            for (i in 0 until itemCount) {
                if (TextUtils.equals(id, getPagerItem(i).id)) {
                    return i
                }
            }
        } catch (e: Exception) {
        }
        return -1
    }

    override fun getItemCount(): Int {
        return if (pagerItems == null || pagerItems!!.isEmpty()) 0 else pagerItems!!.size
    }

    /**
     *标题
     */
    fun getTitle(position: Int) = getPagerItem(position).title

    fun <T : Fragment> getCurrentFragment(): T? {
        return if (mCacheFragment == null)
            mFragments.get(getItemId(mCurrentPosition)) as T?
        else mCacheFragment!![getItemId(mCurrentPosition)] as T?
    }

    /**
     * 获取缓存的fragment
     * 前提是开启缓存
     *
     * @param position
     * @return
     */
    fun <T : Fragment?> getCacheFragment(position: Int): T? {
        val imteId = getItemId(position)
        return if (mCacheFragment == null) mFragments.get(getItemId(position)) as T?
        else mCacheFragment!![imteId] as T?
    }

    /**
     * 清空缓存
     */
    fun clearCache() {
        mCacheFragment?.clear()
    }

    /**
     * 清空缓存
     */
    fun removeCache(position: Int) {
        mCacheFragment?.remove(getItemId(position))
    }

    /**
     * 获取缓存大小
     */
    fun getCacheSize() = mCacheFragment?.size() ?: 0


    /**
     * 构建者
     */
    class Builder private constructor(var fm: FragmentManager,
                                      var mLifecycle: Lifecycle) {
        var items: MutableList<FragmentPagerItem> = ArrayList()
        var isUseCache = false

        private constructor(fragment: Fragment) : this(fragment.childFragmentManager, fragment.lifecycle) {}
        private constructor(fragmentActivity: FragmentActivity) : this(fragmentActivity.supportFragmentManager, fragmentActivity.lifecycle) {}

        fun setItems(items: MutableList<FragmentPagerItem>): Builder {
            this.items = items
            return this
        }

        fun setCache(isCache: Boolean): Builder {
            this.isUseCache = isCache
            return this
        }

        fun addItem(item: FragmentPagerItem): Builder {
            items.add(item)
            return this
        }

        fun addItem(itemPath: String): FragmentPagerItem {
            val item = FragmentPagerItem.get(itemPath)
            item.builder = this
            items.add(item)
            return item
        }

        fun build(): FragmentPagerAdapter {
            return FragmentPagerAdapter(this)
        }

        companion object {
            /**
             * 新版本Fragment
             * 获取焦点（包括滑动）会调用onResume, 失去焦点（包括滑动走了） 会调用 onPause
             *
             * @param fragmentActivity
             * @return
             */
            @JvmStatic
            fun create(fragmentActivity: FragmentActivity): Builder {
                return Builder(fragmentActivity)
            }

            @JvmStatic
            fun create(fragmentManager: FragmentManager,
                       lifecycle: Lifecycle): Builder {
                return Builder(fragmentManager, lifecycle)
            }
        }

    }


}