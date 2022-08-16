package androidx.viewpager2.widget

import androidx.recyclerview.widget.RecyclerView
import com.ashlikun.xviewpager2.ViewPagerUtils

/**
 * 作者　　: 李坤
 * 创建时间: 2022/8/16　13:42
 * 邮箱　　：496546144@qq.com
 *
 * 功能介绍：
 */
/**
 * 设置ViewPager2的LayoutManager
 */
fun ViewPager2.setLayoutManager(layoutManager: RecyclerView.LayoutManager) {
    mRecyclerView.layoutManager = layoutManager
    mRecyclerView.clearOnChildAttachStateChangeListeners() // to ability set exact view size
    ViewPagerUtils.setField(this, "mLayoutManager", layoutManager)
    ViewPagerUtils.setField(mScrollEventAdapter, "mLayoutManager", layoutManager) // to correct work ViewPager2.OnPageChangeCallback
}

/**
 * 设置预留出来一点的效果
 */
open class WidePageLayoutManager(
    viewPager: ViewPager2,
    //子空间占ViewPager 的比例
    val viewSmallPercentage: Float,
    //子空间左右间距
    val margin: Int
) : PageLayoutManagerIml(viewPager) {

    init {
        ViewPagerUtils.getViewSize(viewPager) { width, height ->
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                val margin = ((width - viewSmallPercentage * width) / 2f - margin).toInt()
                recyclerView.setPadding(margin, recyclerView.paddingTop, margin, recyclerView.bottom)
            } else if (viewPager.orientation == ViewPager2.ORIENTATION_VERTICAL) {
                val margin = ((height - viewSmallPercentage * width) / 2f - margin).toInt()
                recyclerView.setPadding(recyclerView.paddingLeft, margin, recyclerView.paddingRight, margin)
            }

            recyclerView.scrollToPosition(viewPager.currentItem)
        }
        recyclerView.clipToPadding = false
    }

    // this need to show MainPage bounds on left/right side menu
    override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
        if (lp != null) {
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                lp.width = (width * viewSmallPercentage).toInt()
                lp.leftMargin = margin
                lp.rightMargin = margin
            } else if (viewPager.orientation == ViewPager2.ORIENTATION_VERTICAL) {
                lp.height = (height * viewSmallPercentage).toInt()
                lp.topMargin = margin
                lp.bottomMargin = margin
            }
        }
        return super.checkLayoutParams(lp)
    }

    override fun canScrollHorizontally(): Boolean {
        return super.canScrollHorizontally() && viewPager.isEnabled
    }
}
