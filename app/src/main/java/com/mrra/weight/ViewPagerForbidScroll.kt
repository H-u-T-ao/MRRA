package com.mrra.weight

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class ViewPagerForbidScroll : ViewPager {

    internal constructor (context: Context)
            : this(context, null)

    @Suppress("ClickableViewAccessibility")
    internal constructor (context: Context, attrs: AttributeSet?)
            : super(context, attrs) {
        setOnTouchListener { _, _ -> true }
    }


    @Suppress("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return false
    }


}