package com.ashlikun.xviewpager2.simple

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.ashlikun.xviewpager2.ConvenientBanner
import com.ashlikun.xviewpager2.indicator.TransIndicator
import com.ashlikun.xviewpager2.listener.OnItemClickListener
import com.ashlikun.xviewpager2.simple.BannerAdapter.RESURL2
import com.ashlikun.xviewpager2.transform.DepthPageTransformer
import com.ashlikun.xviewpager2.transform.MarginMultiPageTransformer
import com.ashlikun.xviewpager2.view.BannerViewPager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        convenientBanner.indicator = TransIndicator(this)
//        bannerViewPager.setAdapter(BannerAdapter1(this))
        convenientBanner.setAdapter(BannerAdapter1(this, RESURL2))

        convenientBanner.setPageTransformer(MarginMultiPageTransformer(50, 150, 150, 0.9f))
        convenientBanner.setOnItemClickListener(object : OnItemClickListener<String> {
            override fun onItemClick(data: String, position: Int) {
                Toast.makeText(this@MainActivity, "" + position, Toast.LENGTH_LONG).show()
            }
        })
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