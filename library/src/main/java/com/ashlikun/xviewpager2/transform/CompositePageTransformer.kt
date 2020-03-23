/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ashlikun.xviewpager2.transform

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import java.util.*

/**
 * Allows for combining multiple [PageTransformer] objects.
 *
 * @see ViewPager2.setPageTransformer
 *
 * @see MarginPageTransformer
 */
class CompositePageTransformer : ViewPager2.PageTransformer {
    private val mTransformers: MutableList<ViewPager2.PageTransformer> = ArrayList()

    /**
     * Adds a page transformer to the list.
     *
     *
     * Transformers will be executed in the order that they were added.
     */
    fun addTransformer(transformer: ViewPager2.PageTransformer) {
        if (!mTransformers.contains(transformer)) {
            mTransformers.add(transformer)
        }
    }

    /** Removes a page transformer from the list.  */
    fun removeTransformer(transformer: ViewPager2.PageTransformer) {
        mTransformers.remove(transformer)
    }

    fun containsTransformer(transformer: ViewPager2.PageTransformer) = mTransformers.contains(transformer)

    /**
     * 屏幕左边的左边 :<-1
     *
     * 屏幕左右：[-1,0]
     * 屏幕右边：[0,1]
     *
     * 屏幕右边的右边 :>1
     */
    override fun transformPage(page: View, position: Float) {
        for (transformer in mTransformers) {
            transformer.transformPage(page, position)
        }
    }
}