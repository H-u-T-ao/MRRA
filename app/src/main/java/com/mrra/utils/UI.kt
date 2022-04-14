package com.mrra.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import kotlin.math.ceil

fun Context.isLightTheme() = resources.configuration.uiMode == 0x11

fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier(
        "status_bar_height",
        "dimen",
        "android"
    )
    if (resourceId > 0) {
        result = resources.getDimensionPixelSize(resourceId)
    }
    return result
}

fun Context.getNavigateBarHeight(): Int {
    resources.run {
        return if (
            getIdentifier(
                "config_showNavigationBar",
                "bool",
                "android"
            ) != 0
        ) {
            getDimensionPixelSize(
                getIdentifier(
                    "navigation_bar_height",
                    "dimen",
                    "android"
                )
            )
        } else {
            0
        }
    }
}

// 开启沉浸式状态栏和导航栏
fun Activity.clearSystemWindows(
    statusBar: TextView? = null,
    color: Int = Color.TRANSPARENT
) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    statusBar?.height = getStatusBarHeight()
    window.statusBarColor = color
    window.navigationBarColor = Color.TRANSPARENT
}

fun Activity.clearSystemWindowsExceptNavigateBar(
    statusBar: TextView? = null
) {
    val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN.or(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
    window.decorView.systemUiVisibility = option
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    statusBar?.height = getStatusBarHeight()
    window.statusBarColor = Color.TRANSPARENT
}

// 设置状态栏文本颜色
fun Activity.setStatusBarTextColor(isWhite: Boolean) {
    ViewCompat.getWindowInsetsController(window.decorView)
        ?.isAppearanceLightStatusBars = !isWhite
}

fun Activity.getScreenShot(): Bitmap {
    return window.decorView.let { view ->
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
//        val width = resources.displayMetrics.widthPixels
//        val height = resources.displayMetrics.heightPixels
        val bitmap = Bitmap.createBitmap(view.drawingCache/*, 0, 0, width, height*/)
        view.destroyDrawingCache()
        bitmap
    }
}

fun Context.getBlurBitmap(radius: Float, bitmap: Bitmap): Bitmap {
    val renderScript = RenderScript.create(this)
    val input = Allocation.createFromBitmap(renderScript, bitmap)
    val output = Allocation.createTyped(renderScript, input.type)
    val scriptIntrinsicBlur =
        ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    scriptIntrinsicBlur.setRadius(radius)
    scriptIntrinsicBlur.setInput(input)
    scriptIntrinsicBlur.forEach(output)
    output.copyTo(bitmap)
    return bitmap
}
