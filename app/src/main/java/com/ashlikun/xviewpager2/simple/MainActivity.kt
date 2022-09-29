package com.ashlikun.xviewpager2.simple

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.ashlikun.adapter.ViewHolder
import com.ashlikun.adapter.recyclerview.CommonAdapter
import com.ashlikun.glideutils.GlideUtils
import com.ashlikun.xviewpager2.simple.BannerAdapter.RESURL2
import com.ashlikun.xviewpager2.transform.MarginMultiPageTransformer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val adapter by lazy {
        object : CommonAdapter<String>(this, RESURL2) {
            override fun getLayoutId() = R.layout.imem_image_view
            override fun convert(holder: ViewHolder, t: String) {
                val imageView = holder.getImageView(R.id.imageView)
                GlideUtils.show(imageView, t)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        convenientBanner.indicator = TransIndicator(this)
//        bannerViewPager.setAdapter(BannerAdapter1(this))
        convenientBanner.adapter = adapter

        convenientBanner.setPageTransformer(MarginMultiPageTransformer(50, 150, 150, 0.9f))
//        convenientBanner.setOnItemClickListener(object : OnItemClickListener<String> {
//            override fun onItemClick(data: String, position: Int) {
//                Toast.makeText(this@MainActivity, "" + position, Toast.LENGTH_LONG).show()
//            }
//        })
        convenientBanner.setCurrentItem(1, false)
        convenientBanner.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("MainActivity", "  $position")
            }
        })
    }

    fun onClick(view: View?) {
        val intent = Intent(this, Main2Activity::class.java)
        startActivity(intent)
        //        if (bannerViewPager.getRealItemCount() == RESURL.length) {
//            bannerViewPager.setPages(new ArrayList(Arrays.asList(RESURL2)));
//            convenientBanner.setPages(new ArrayList(Arrays.asList(RESURL2)));
//        } else {
//            bannerViewPager.setPages(new ArrayList(Arrays.asList(RESURL)));
//            convenientBanner.setPages(new ArrayList(Arrays.asList(RESURL)));
//        }
    }
}