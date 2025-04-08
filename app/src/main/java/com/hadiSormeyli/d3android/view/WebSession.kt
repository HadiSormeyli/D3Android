package com.hadiSormeyli.d3android.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.webkit.WebResourceErrorCompat
import androidx.webkit.WebViewClientCompat


internal class WebSession @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : WebView(context, attrs, defStyle) {

    init {
        layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        visibility = View.INVISIBLE

        @SuppressLint("SetJavaScriptEnabled")
        settings.javaScriptEnabled = true
        settings.domStorageEnabled = true
    }

    fun onSessionReady(onReady: () -> Unit) {
        webViewClient = object : WebViewClientCompat() {
            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceErrorCompat
            ) {
                super.onReceivedError(view, request, error)
                Log.d("Tag", "Loading error")
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.d("Tag", "Loading finished")
                visibility = View.VISIBLE
                onReady()
            }
        }
    }
}