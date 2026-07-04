package com.example.f1live

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.view.View
import android.view.WindowInsets

class MainActivity : Activity() {
    
    private lateinit var webView: WebView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // 初始化 WebView
        webView = findViewById(R.id.webview)
        
        // 配置 WebView
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true  // 启用 JavaScript（倒计时需要）
        webSettings.domStorageEnabled = true   // 启用 DOM 存储
        webSettings.setSupportZoom(false)      // 禁止缩放
        webSettings.builtInZoomControls = false
        
        // 全屏模式（隐藏状态栏和导航栏）
        enableFullscreen()
        
        // 设置 WebViewClient（在同一个 WebView 中打开链接）
        webView.webViewClient = WebViewClient()
        
        // 加载本地 HTML 文件
        loadLocalHTML()
    }
    
    private fun loadLocalHTML() {
        // 加载 assets 目录下的 HTML 文件
        webView.loadUrl("file:///android_asset/jrs_wlty_online.html")
    }
    
    @Suppress("DEPRECATION")
    private fun enableFullscreen() {
        // 状态栏和导航栏透明
        window.statusBarColor = Color.TRANSPARENT
        window.navigationBarColor = Color.TRANSPARENT
        
        // 使用新 API 实现全屏（Android 11+ / API 30+）
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            window.insetsController?.let { controller ->
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // 旧 API（Android 10 及以下）
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
            )
        }
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            enableFullscreen()
        }
    }
    
    // 处理返回键（在 WebView 中返回上一页）
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
    
    override fun onDestroy() {
        // 清理 WebView
        webView.destroy()
        super.onDestroy()
    }
}
