package com.example.f1live

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : Activity() {
    
    private lateinit var webView: WebView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)
            
            // 初始化 WebView
            webView = findViewById(R.id.webview)
            
            // 配置 WebView
            val webSettings: WebSettings = webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.domStorageEnabled = true
            webSettings.setSupportZoom(false)
            webSettings.builtInZoomControls = false
            
            // 全屏模式
            enableFullscreen()
            
            // 设置 WebViewClient
            webView.webViewClient = WebViewClient()
            
            // 加载本地 HTML 文件
            loadLocalHTML()
        } catch (e: Exception) {
            Log.e("F1Live", "onCreate error: ${e.message}", e)
            Toast.makeText(this, "启动失败: ${e.message}", Toast.LENGTH_LONG).show()
            finish()
        }
    }
    
    private fun loadLocalHTML() {
        try {
            val url = "file:///android_asset/jrs_wlty_online.html"
            Log.d("F1Live", "Loading URL: $url")
            webView.loadUrl(url)
        } catch (e: Exception) {
            Log.e("F1Live", "loadLocalHTML error: ${e.message}", e)
            Toast.makeText(this, "加载页面失败: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    @Suppress("DEPRECATION")
    private fun enableFullscreen() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                window.setDecorFitsSystemWindows(false)
                window.insetsController?.let { controller ->
                    controller.hide(
                        android.view.WindowInsets.Type.statusBars() or
                        android.view.WindowInsets.Type.navigationBars()
                    )
                    controller.systemBarsBehavior =
                        android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            } else {
                @Suppress("DEPRECATION")
                window.decorView.systemUiVisibility = (
                    android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                )
            }
        } catch (e: Exception) {
            Log.e("F1Live", "enableFullscreen error: ${e.message}", e)
        }
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            enableFullscreen()
        }
    }
    
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
        try {
            webView.destroy()
        } catch (e: Exception) {
            Log.e("F1Live", "webView.destroy() error: ${e.message}", e)
        }
        super.onDestroy()
    }
}
