package androidx.viewpager2.widget

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 作者　　: 李坤
 * 创建时间: 2022/8/16　15:13
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */


/**
 * ViewPager2 的 LinearLayoutManagerImpl
 */
@SuppressLint("WrongConstant")
open class PageLayoutManagerIml(
    val viewPager: ViewPager2
) : LinearLayoutManager(viewPager.context, viewPager.orientation, false) {
    val recyclerView by lazy {
        viewPager.mRecyclerView
    }

    init {
    }

    fun getPageSize(): Int {
        val rv = recyclerView
        return if (orientation == ViewPager2.ORIENTATION_HORIZONTAL) rv.width - rv.paddingLeft - rv.paddingRight else rv.height - rv.paddingTop - rv.paddingBottom
    }

    // copied from ViewPager2.LinearLayoutManagerImp
    override fun calculateExtraLayoutSpace(state: RecyclerView.State, extraLayoutSpace: IntArray) {
        val pageLimit: Int = viewPager.offscreenPageLimit
        val offscreenSpace: Int = getPageSize() * pageLimit
        extraLayoutSpace[0] = offscreenSpace
        extraLayoutSpace[1] = offscreenSpace
    }

    // copied from ViewPager2.LinearLayoutManagerImp
    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean,
        focusedChildVisible: Boolean
    ): Boolean {
        return false // users should use setCurrentItem instead
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() && viewPager.isEnabled
    }

    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && viewPager.isEnabled
    }
}