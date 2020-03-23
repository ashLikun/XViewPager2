package com.ashlikun.xviewpager2

import androidx.fragment.app.FragmentManager

/**
 * 作者　　: 李坤
 * 创建时间: 2019/4/11　10:05
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
object FragmentUtils {
    /**
     * 初始化时候先清空全部，防止内存不足时候再次创建
     */
    fun removeAll(fm: FragmentManager) {
        val ff = fm.fragments
        if (ff != null && !ff.isEmpty()) {
            val ft = fm.beginTransaction()
            for (f in ff) {
                ft.remove(f!!)
            }
            ft.commitNowAllowingStateLoss()
        }
    }
}