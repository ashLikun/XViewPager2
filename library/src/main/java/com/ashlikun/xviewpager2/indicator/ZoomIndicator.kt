package com.ashlikun.xviewpager2.indicator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView

/**
 * @author　　: 李坤
 * 创建时间: 2020/3/13 13:21
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：带缩放改变的Indicator，根据滑动多少缩放多少，只缩放大小
 */
class ZoomIndicator @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    IBannerIndicator(context!!, attrs, defStyleAttr) {
    companion object {
        private const val ALPHA_MAX = 1.0f
        private const val SCALE_MIN = 1.0f
        private const val ANIM_OUT_TIME = 400
        private const val ANIM_IN_TIME = 300
    }

    private val mAlpha_min = 0.8f
    private val mScale_max = 1.4f


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var height = measuredHeight
        height = height.coerceAtLeast(noSelectDraw?.intrinsicHeight?.coerceAtLeast(selectDraw?.intrinsicHeight
            ?: 0) ?: 0)
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (height * mScale_max * 1.1f).toInt())
    }

    override fun initView(context: Context?, attrs: AttributeSet?) {
        clipChildren = false
    }

    /**
     * 底部指示器资源图片
     *
     * @param
     */
    override fun notifyDataSetChanged(selectIndex: Int): ZoomIndicator? {
        removeAllViews()
        pointViews.clear()
        if (dataCount == 0) {
            return this
        }
        val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.setMargins(space, 0, space, 0)
        (0 until dataCount).forEach { index ->
            // 翻页指示的点
            val pointView: View = ImageView(context)
            if (selectIndex == index) {
                pointView.background = selectDraw
                pointView.isSelected = true
            } else {
                pointView.background = noSelectDraw
                pointView.isSelected = false
            }
            pointViews.add(pointView)
            addView(pointView, params)
        }
        return this
    }

    override fun onPointScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPointSelected(selectIndex: Int) {
        pointViews.forEachIndexed { index, view ->
            if (view.isSelected) {
                view.background = noSelectDraw
                targetViewAnim(view, false)
            }
        }
        pointViews.getOrNull(selectIndex)?.also {
            Log.e("ddddddd222", "ss ${selectIndex}")
            it.background = selectDraw
            targetViewAnim(it, true)
        }
    }

    /**
     * 用于小圆点的放大缩小
     *
     * @param view
     * @param isMax 是不是变大
     */
    private fun targetViewAnim(view: View, isMax: Boolean) {
        (view.getTag(36987418) as? AnimatorSet)?.cancel()
        view.isSelected = isMax
        val animatorSet = AnimatorSet()
        var scaleX: ObjectAnimator? = null
        var scaleY: ObjectAnimator? = null
        var alpha: ObjectAnimator? = null
        view.alpha = 1f
        if (isMax) {
            scaleX = ObjectAnimator.ofFloat(view, "scaleX", SCALE_MIN, mScale_max)
            scaleY = ObjectAnimator.ofFloat(view, "scaleY", SCALE_MIN, mScale_max)
            alpha = ObjectAnimator.ofFloat(view, "alpha", mAlpha_min, ALPHA_MAX)
            animatorSet.duration = ANIM_OUT_TIME.toLong()
        } else {
            view.scaleX = 1f
            view.scaleY = 1f
            scaleX = ObjectAnimator.ofFloat(view, "scaleX", mScale_max, SCALE_MIN)
            scaleY = ObjectAnimator.ofFloat(view, "scaleY", mScale_max, SCALE_MIN)
            alpha = ObjectAnimator.ofFloat(view, "alpha", ALPHA_MAX, mAlpha_min)
            animatorSet.duration = ANIM_IN_TIME.toLong()
        }
        animatorSet.play(scaleX).with(scaleY).with(alpha)
        animatorSet.interpolator = AccelerateDecelerateInterpolator()
        animatorSet.start()
        view.setTag(36987418, animatorSet)
    }


}