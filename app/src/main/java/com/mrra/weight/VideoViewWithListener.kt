package com.mrra.weight

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

class VideoViewWithListener : VideoView {

    internal constructor (context: Context)
            : this(context, null)

    internal constructor (context: Context, attrs: AttributeSet?)
            : this(context, attrs, 0)

    internal constructor (context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    fun setStartListener(listener: () -> Unit) {
        startListener = listener
    }

    private var startListener: (() -> Unit)? = null

    override fun start() {
        super.start()
        startListener?.invoke()
    }

}