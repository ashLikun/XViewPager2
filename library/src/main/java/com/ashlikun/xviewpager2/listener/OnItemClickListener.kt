package com.ashlikun.xviewpager2.listener

/**
 * @author　　: 李坤
 * 创建时间: 2018/8/10 14:23
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：Banner的item点击事件
 */
interface OnItemClickListener<T> {
    fun onItemClick(data: T, position: Int)
}