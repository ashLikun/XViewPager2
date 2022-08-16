package com.ashlikun.xviewpager2.simple

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.ashlikun.xviewpager2.fragment.FragmentPagerAdapter
import com.ashlikun.xviewpager2.fragment.FragmentPagerItem
import com.ashlikun.xviewpager2.listener.OnItemClickListener
import com.ashlikun.xviewpager2.transform.MarginMultiPageTransformer
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.fragment_test2.*
import kotlinx.android.synthetic.main.fragment_test2.fragmentLayout

/**
 * 作者　　: 李坤
 * 创建时间: 2019/4/10　17:41
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
@Route(path = "/Fragment/test2")
class Test2Fragment : Fragment() {
    var id: String? = null
    var rootView: View? = null
    var isCache = false
    val adapter: FragmentPagerAdapter by lazy {
        FragmentPagerAdapter.Builder.create(childFragmentManager, lifecycle)
            .addItem("/Fragment/test").setId("111").ok()
            .addItem("/Fragment/test").setId("222").ok()
            .addItem("/Fragment/test").setId("333").ok()
            .addItem("/Fragment/test").setId("444").ok()
            .addItem("/Fragment/test").setId("555").ok()
            .addItem("/Fragment/test").setId("666").ok()
            .addItem("/Fragment/test").setId("777").ok()
            .setCache(true)
            .build()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        id = arguments!!.getString(FragmentPagerItem.ID)
        Log.e("onAttach", "id == $id")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_test2, null)
        } else {
            val parent = rootView!!.parent as ViewGroup
            parent?.removeView(rootView)
        }
        Log.e("onCreateView", "id == $id    isCache = $isCache")
        isCache = true
        return rootView
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)
        fragmentLayout.setOffscreenPageLimit(2)
        fragmentLayout.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.e("onActivityCreated", "id == $id")
    }

    override fun onResume() {
        super.onResume()
        Log.e("onResume", "id == $id")
    }

    override fun onPause() {
        super.onPause()
        Log.e("onPause", "id == $id")
    }

    override fun onStop() {
        super.onStop()
        Log.e("onStop", "id == $id")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e("onDestroyView", "id == $id")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("onDestroy", "id == $id")
    }

    override fun onDetach() {
        super.onDetach()
        Log.e("onDetach", "id == $id")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.e("onHiddenChanged", "id == $id   hidden = $hidden")
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        id = arguments!!.getString(FragmentPagerItem.ID)
        Log.e("setUserVisibleHint", "id == $id   isVisibleToUser = $isVisibleToUser")
    }


    fun onClick1(view: View?) {
        fragmentLayout.setCurrentItem(0)
    }

    fun onClick2(view: View?) {
        fragmentLayout.setCurrentItem(1)
    }

    fun onClick3(view: View?) {
        fragmentLayout.setCurrentItem(2)
    }

    fun onClick4(view: View?) {
        fragmentLayout.setCurrentItem(3)
    }

    fun onClick5(view: View?) {
        fragmentLayout.setCurrentItem(4)
    }

    fun onClick6(view: View?) {
        fragmentLayout.setCurrentItem(5)
    }

    fun onClick7(view: View?) {
        fragmentLayout.setCurrentItem(6)
    }
}