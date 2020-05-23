package com.ashlikun.xviewpager2.simple

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.ashlikun.xviewpager2.fragment.FragmentPagerItem
import com.ashlikun.xviewpager2.listener.OnItemClickListener
import com.ashlikun.xviewpager2.transform.MarginMultiPageTransformer
import kotlinx.android.synthetic.main.fragment_test1.*

/**
 * 作者　　: 李坤
 * 创建时间: 2019/4/10　17:41
 * 邮箱　　：496546144@qq.com
 *
 *
 * 功能介绍：
 */
@Route(path = "/Fragment/test")
class TestFragment : Fragment() {
    var id: String? = null
    var rootView: View? = null
    var isCache = false
    var count = 0
    override fun onAttach(context: Context) {
        super.onAttach(context)
        id = arguments!!.getString(FragmentPagerItem.ID)
        Log.e("onAttach", "id == $id")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("onCreate", "id == $id")
        lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                Log.e("aaaaaaaa onDestroy", "id == $id")
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_test1, null)
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
        textView.text = "id == $id"
        Log.e("onViewCreated", "id == $id")
//        textView.text = "我是第" + id + "个"
//        when (id) {
//            "1" -> bagView!!.setBackgroundColor(-0x10000)
//            "2" -> bagView!!.setBackgroundColor(-0xff0100)
//            "3" -> bagView!!.setBackgroundColor(-0xffff01)
//            "4" -> bagView!!.setBackgroundColor(-0xf001)
//            "5" -> bagView!!.setBackgroundColor(-0x99aa78)
//            "6" -> bagView!!.setBackgroundColor(-0x8877de)
//            "7" -> bagView!!.setBackgroundColor(-0xccbb67)
//        }
        recycleView.recyclerView.layoutManager = LinearLayoutManager(context)
        recycleView.recyclerView.adapter = MyListAdapter(context!!)

        convenientBanner.setAdapter(BannerAdapter1(context!!, if (id?.toInt() ?: 0 > 666) BannerAdapter.RESURL3 else BannerAdapter.RESURL2))

        convenientBanner.setPageTransformer(MarginMultiPageTransformer(50, 150, 150, 0.9f))
        convenientBanner.setOnItemClickListener(object : OnItemClickListener<String> {
            override fun onItemClick(data: String, position: Int) {
            }
        })
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
}