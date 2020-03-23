package com.ashlikun.xviewpager2.simple

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 作者　　: 李坤
 * 创建时间: 2019/4/11　9:48
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
    }
}