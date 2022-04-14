package com.mrra.weight

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.RelativeLayout

class ViewPager2TouchListener : RelativeLayout {

    internal constructor (context: Context)
            : this(context, null)

    internal constructor (context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    internal constructor (context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    private var listener: ((MotionEvent) -> Unit)? = null

    fun setTouchListener(listener: (MotionEvent) -> Unit) {
        this.listener = listener
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        listener?.invoke(event)
        return false
    }

}