package com.mrra.activity.web

import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mrra.R
import com.mrra.base.BaseActivity
import com.mrra.utils.clearSystemWindows
import com.mrra.utils.isLightTheme
import com.mrra.utils.setStatusBarTextColor

class WebActivity : BaseActivity() {

    private lateinit var statusBar: TextView
    private lateinit var title: TextView
    private lateinit var webView: WebView
    private lateinit var back: CardView
    private lateinit var pb: ProgressBar

    @Suppress("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        statusBar = findViewById(R.id.tv_web_status_bar)
        title = findViewById(R.id.tv_web_title)
        webView = findViewById(R.id.wv_web_web_view)
        back = findViewById(R.id.cv_web_back)
        pb = findViewById(R.id.pb_web_pb)

        val url = intent.getStringExtra("url") ?: ""
        webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, titleText: String) {
                title.text = titleText
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                pb.progress = newProgress
                pb.visibility =
                    if (newProgress >= 99) View.GONE
                    else View.VISIBLE
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
        webView.loadUrl(url)

        back.setOnClickListener {
            if (webView.canGoBack()) webView.goBack()
            else finish()
        }
    }

    override fun onResume() {
        super.onResume()
        clearSystemWindows(
            statusBar,
            if (isLightTheme())
                ContextCompat.getColor(
                    this,
                    R.color.theme_style_light
                )
            else
                ContextCompat.getColor(
                    this,
                    R.color.dark_theme_style_light
                )
        )
        setStatusBarTextColor(!isLightTheme())
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.clearCache(true)
    }

}