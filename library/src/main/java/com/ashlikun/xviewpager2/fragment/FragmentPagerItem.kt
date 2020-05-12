package com.ashlikun.xviewpager2.fragment

import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import java.io.Serializable

/**
 * 作者　　: 李坤
 * 创建时间: 2018/6/15 0015　下午 1:44
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：[FragmentPagerAdapter]
 * ViewPager的Fragment 适配器的item参数
 */
class FragmentPagerItem private constructor(
        /**
         * 必须参数,路由路径
         */
        var path: String) : Serializable {

    /**
     * fragment 的title,
     */
    var title: CharSequence? = null
        protected set

    /**
     * id,缓存数据
     */
    var id: String? = null
        protected set

    /**
     * 可null，携带的参数
     */
    var param: Bundle? = null
        protected set

    /**
     * 零时的
     */
    var builder: FragmentPagerAdapter.Builder? = null

    fun setPath(path: String): FragmentPagerItem {
        this.path = path
        return this
    }

    fun setTitle(title: CharSequence?): FragmentPagerItem {
        this.title = title
        return this
    }

    fun setId(id: String?): FragmentPagerItem {
        this.id = id
        addParam(ID, id)
        return this
    }

    fun setParam(param: Bundle?): FragmentPagerItem {
        this.param = param
        return this
    }

    fun addParam(key: String?, value: Any?): FragmentPagerItem {
        if (TextUtils.isEmpty(key) || value == null) {
            return this
        }
        if (param == null) {
            param = Bundle()
        }
        if (param!!.containsKey(key)) {
            return this
        }
        if (value is String) {
            param!!.putString(key, value as String?)
        } else if (value is Int) {
            param!!.putInt(key, (value as Int?)!!)
        } else if (value is Float) {
            param!!.putFloat(key, (value as Float?)!!)
        } else if (value is Short) {
            param!!.putShort(key, (value as Short?)!!)
        } else if (value is Boolean) {
            param!!.putBoolean(key, (value as Boolean?)!!)
        } else if (value is Serializable) {
            param!!.putSerializable(key, value as Serializable?)
        } else if (value is IntArray) {
            param!!.putIntArray(key, value as IntArray?)
        } else if (value is Array<*>) {
            param!!.putStringArray(key, value as Array<String?>?)
        } else if (value is FloatArray) {
            param!!.putFloatArray(key, value as FloatArray?)
        } else if (value is Parcelable) {
            param!!.putParcelable(key, value as Parcelable?)
        }
        return this
    }

    /**
     * 回退到构建的地方
     *
     * @return
     */
    fun ok(): FragmentPagerAdapter.Builder {
        val res = builder
        builder = null
        return res!!
    }

    companion object {
        const val ID = "id"
        operator fun get(path: String): FragmentPagerItem {
            return FragmentPagerItem(path)
        }
    }

}