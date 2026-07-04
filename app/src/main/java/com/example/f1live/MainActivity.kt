package com.example.f1live

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
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
        
        // 强制横屏（已经在 Manifest 里设置，这里再次确保）
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        
        // 保持屏幕常亮（投影仪重要）
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        // 隐藏状态栏和导航栏（全屏模式）
        hideSystemUI()
        
        // 设置 WebViewClient（在同一个 WebView 中打开链接）
        webView.webViewClient = WebViewClient()
        
        // 加载本地 HTML 文件
        loadLocalHTML()
    }
    
    private fun loadLocalHTML() {
        // 加载 assets 目录下的 HTML 文件
        webView.loadUrl("file:///android_asset/jrs_wlty_online.html")
    }
    
    private fun hideSystemUI() {
        // 全屏模式（隐藏状态栏和导航栏）
        window.decorView.systemUiVisibility = (
            android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
    
    // 处理返回键（在 WebView 中返回上一页）
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
    
    override fun onDestroy() {
        // 清理 WebView
        webView.destroy()
        super.onDestroy()
    }
}
