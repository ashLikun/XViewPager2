
[![Release](https://jitpack.io/v/ashLikun/XViewPager2.svg)](https://jitpack.io/#ashLikun/XViewPager2)

# **XViewPager2**

## 使用方法

build.gradle文件中添加:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
并且:

```gradle
dependencies {
    implementation 'com.github.ashLikun:XViewPager2:{latest version}'//XViewPager2
}
```
### 1.Banner用法
```java
         <com.ashlikun.xviewpager2.ConvenientBanner
                             android:id="@+id/convenientBanner"
                             android:layout_width="0dp"
                             android:layout_height="400dp"
                             android:layout_gravity="center_horizontal"
                             android:layout_marginTop="10dp"
                             android:orientation="vertical"
                             app:banner_isAutoTurning="false"
                             app:banner_isOneDataOffLoopAndTurning="true"
                             app:banner_turningTime="2500"
                             app:ind_no_select="@drawable/my_banner_circle_default"
                             app:ind_select="@drawable/my_banner_circle_select"
                             app:ind_space="10dp"
                             app:ind_style="ind_trans"
                             app:xvp_radius="20dp"
                             app:xvp_ratio_orientation="height" />

         <!--是否可以循环 默认true-->
             <attr name="banner_canLoop" format="boolean" />
             <!--自动播放时间间隔 默认5000-->
             <attr name="banner_turningTime" format="integer" />
             <!--是否可以自动定时翻页-->
             <attr name="banner_isAutoTurning" format="boolean" />
             <!--是否只有一条数据的时候禁用翻页, 默认true-->
             <attr name="banner_isOneDataOffLoopAndTurning" format="boolean" />
         
             <!--Indicatorde 属性-->
             <!--对齐方式-->
             <attr name="ind_gravity">
                 <flag name="top" value="0x30" />
                 <flag name="bottom" value="0x50" />
                 <flag name="left" value="0x03" />
                 <flag name="right" value="0x05" />
                 <flag name="center_vertical" value="0x10" />
                 <flag name="center_horizontal" value="0x01" />
                 <flag name="center" value="0x11" />
             </attr>
             <!--间距-->
             <attr name="ind_space" format="dimension" />
             <!--未选中的资源Id-->
             <attr name="ind_no_select" format="reference" />
             <!--选中的资源Id-->
             <attr name="ind_select" format="reference" />
             <!--比例   默认16/9.0f-->
             <attr name="xvp_ratio" format="reference|float"></attr>
             <!-- 比例参考方向-->
             <attr name="xvp_ratio_orientation">
                 <enum name="width" value="0" />
                 <enum name="height" value="1" />
             </attr>
             <!-- 页面切换速度,值越大滑动越慢，滑动太快会使3d效果不明显 Ms     -->
             <attr name="xvp_scrollDuration" format="integer"></attr>
             <!--圆角半径-->
             <attr name="xvp_radius" format="reference|dimension"></attr>
             <attr name="xvp_radiusLeftTop" format="reference|dimension"></attr>
             <attr name="xvp_radiusRightTop" format="reference|dimension"></attr>
             <attr name="xvp_radiusRightBottom" format="reference|dimension"></attr>
             <attr name="xvp_radiusLeftBottom" format="reference|dimension"></attr>
             <!--是否可以滑动-->
             <attr name="xvp_isCanTouchScroll" format="boolean"></attr>

        convenientBanner.setAdapter(BannerAdapter1(this, RESURL2))
        convenientBanner.setPageTransformer(MarginMultiPageTransformer(50, 150, 150, 0.9f))
        convenientBanner.setOnItemClickListener(object : OnItemClickListener<String> {
        override fun onItemClick(data: String, position: Int) {
                 Toast.makeText(this@MainActivity, "" + position, Toast.LENGTH_LONG).show()
            }
        })

      class BannerAdapter1 @JvmOverloads constructor(context: Context, datas: List<String> = BannerAdapter.RESURL) : BasePageAdapter<String>(context, datas) {
          override fun convert(holder: MyViewHolder, data: String) {
              val imageView = holder.itemView as ImageView
              GlideUtils.show(imageView, data)
          }
      
          override fun createView(context: Context): View {
              val imageView = ImageView(context)
              imageView.scaleType = ImageView.ScaleType.CENTER_CROP
              imageView.layoutParams = ViewGroup.LayoutParams(-1, -1)
              return imageView
          }
      }
 
```
### 2.Fragment用法
```java
         <com.ashlikun.xviewpager2.view.XViewPager
                    android:id="@+id/fragmentLayout"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:orientation="horizontal"
                    app:xvp_radius="10dp"
                    app:xvp_ratio="1"></com.ashlikun.xviewpager2.view.XViewPager>

             val adapter: FragmentPagerAdapter by lazy {
                    FragmentPagerAdapter.Builder.create(this)
                            .addItem("/Fragment/test").setId("1").ok()
                            .addItem("/Fragment/test").setId("2").ok()
                            .addItem("/Fragment/test").setId("3").ok()
                            .addItem("/Fragment/test").setId("4").ok()
                            .addItem("/Fragment/test").setId("5").ok()
                            .addItem("/Fragment/test").setId("6").ok()
                            .addItem("/Fragment/test").setId("7").ok()
                            .setCache(true)
                            .build()
                }

          FragmentUtils.removeAll(supportFragmentManager)
                 swipeRefresh.setOnRefreshListener {
                     Handler().postDelayed({
                         swipeRefresh.isRefreshing = false
                     }, 1000)
                 }
                 fragmentLayout.setOffscreenPageLimit(2)
                 fragmentLayout.setAdapter(adapter)
                 fragmentLayout.setRefreshLayout(swipeRefresh)

            TabLayoutMediator(tabLayout,fragmentLayout,adapter).attach

            TabLayoutMediator:与TabLayout一起使用
```
### 混肴
####


