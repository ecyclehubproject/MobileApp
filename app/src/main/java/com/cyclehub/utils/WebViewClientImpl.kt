package com.cyclehub.utils

import android.app.Activity
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient


class WebViewClientImpl(activity: Activity?) : WebViewClient() {
    private var activity: Activity? = null
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        // Do not open new browser to load new link, load them in this webview
        return false
    }


    init {
        this.activity = activity
    }
}