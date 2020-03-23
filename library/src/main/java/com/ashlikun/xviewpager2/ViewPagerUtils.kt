package com.ashlikun.xviewpager2

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.ashlikun.xviewpager2.adapter.BasePageAdapter
import com.ashlikun.xviewpager2.view.XViewPager
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.util.*

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/3 17:38
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：ViewPager的工具
 */
object ViewPagerUtils {
    @JvmStatic
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * 从一个Item获取ViewPager2
     */
    fun getViewPager2(page: View): ViewPager2? {
        val parent = page.parent
        val parentParent = parent.parent
        if (parent is RecyclerView && parentParent is ViewPager2) {
            return parentParent
        }
        return null
    }

    /**
     * 从一个Item获取XViewPager
     */
    fun getXViewPager(page: View): XViewPager? {
        val parent = page.parent
        val parentParent = parent.parent
        if (parent is RecyclerView && parentParent is ViewPager2) {
            val parentParentParent = parentParent.parent
            if (parentParentParent is XViewPager)
                return parentParentParent
        }
        return null
    }

    /**
     * 方法功能：从context中获取activity，如果context不是activity那么久返回null
     */
    fun getActivity(context: Context?): Activity? {
        if (context == null) {
            return null
        }
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper) {
            return getActivity(context.baseContext)
        }
        return null
    }

    /**
     * 方法功能：从context中获取Lifecycle，如果context不是activity那么久返回null
     */
    fun getLifecycle(context: Context?): Lifecycle? {
        val ac = getActivity(context)
        if (ac is LifecycleOwner) {
            return ac.lifecycle
        }
        return null
    }

    /**
     * 无线循环的时候根据position返回真实的position
     *
     * @param position
     * @param allNumber 全部的数量  就是getItemCount
     * @return
     */
    fun getRealPosition(position: Int, allNumber: Int): Int {
        if (allNumber <= BasePageAdapter.MULTIPLE_COUNT * 2 + 1) {
            return 0
        }
        if (position < BasePageAdapter.MULTIPLE_COUNT) {
            var diff = BasePageAdapter.MULTIPLE_COUNT - position
            //最后一个
            return allNumber - BasePageAdapter.MULTIPLE_COUNT * 2 - diff
        } else if (position >= allNumber - BasePageAdapter.MULTIPLE_COUNT) {
            //第一个
            return position - (allNumber - BasePageAdapter.MULTIPLE_COUNT)
        }
        return position - BasePageAdapter.MULTIPLE_COUNT
    }

    /**
     * 获取指定的字段
     */
    fun getAllDeclaredField(claxx: Class<*>?, fieldName: String): Field? {
        var claxx = claxx
        if (fieldName == null || fieldName.isEmpty()) {
            return null
        }
        while (claxx != null && claxx != Any::class.java) {
            try {
                val f = claxx.getDeclaredField(fieldName)
                if (f != null) {
                    return f
                }
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            }
            claxx = claxx.superclass
        }
        return null
    }

    /**
     * 反射字段
     *
     * @param obj    要反射的对象
     * @param fieldName 要反射的字段名称
     */
    fun getField(obj: Any, fieldName: String): Any? {
        try {
            val field = getAllDeclaredField(obj.javaClass, fieldName)
            if (field != null) {
                field.isAccessible = true
                return field[obj]
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 反射字段
     *
     * @param obj    要反射的对象
     * @param fieldName 要反射的字段名称
     * @param value 设置这个字段值
     */
    fun setField(obj: Any, fieldName: String, value: Any): Field? {
        try {
            val field = getAllDeclaredField(obj.javaClass, fieldName)
            if (field != null) {
                field.isAccessible = true
                field.set(obj, value)
                return field
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取全部Method，包括父类
     *
     * @param claxx
     * @return
     */
    fun getAllDeclaredMethods(claxx: Class<*>?): LinkedList<Method>? {
        // find all field.
        var claxx = claxx
        val fieldList = LinkedList<Method>()
        while (claxx != null && claxx != Any::class.java) {
            val fs = claxx.declaredMethods
            for (i in fs.indices) {
                val f = fs[i]
                fieldList.addLast(f)
            }
            claxx = claxx.superclass
        }
        return fieldList
    }

    /**
     * 获取指定的方法
     */
    fun getAllDeclaredMethod(claxx: Class<*>?, methodName: String): Method? {
        var claxx = claxx
        if (methodName == null || methodName.isEmpty()) {
            return null
        }
        while (claxx != null && claxx != Any::class.java) {
            try {
                val f = claxx.getDeclaredMethod(methodName)
                if (f != null) {
                    return f
                }
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
            claxx = claxx.superclass
        }
        return null
    }

    /**
     * 反射方法
     *
     * @param object     要反射的对象
     * @param methodName 要反射的方法名称
     */
    fun getMethod(obj: Any?, methodName: String, vararg args: Any?): Any? {
        if (obj == null) {
            return null
        }
        try {
            val method = getAllDeclaredMethod(obj!!.javaClass, methodName)
            if (method != null) {
                method.isAccessible = true
                if (args.isNotEmpty()) {
                    return method.invoke(obj, args)
                } else {
                    return method.invoke(obj)
                }
            }
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        return null
    }
}