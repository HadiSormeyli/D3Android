package com.hadiSormeyli.d3android.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.webkit.WebViewCompat
import androidx.webkit.WebViewFeature.CREATE_WEB_MESSAGE_CHANNEL
import androidx.webkit.WebViewFeature.POST_WEB_MESSAGE
import androidx.webkit.WebViewFeature.WEB_MESSAGE_PORT_POST_MESSAGE
import androidx.webkit.WebViewFeature.WEB_MESSAGE_PORT_SET_MESSAGE_CALLBACK
import androidx.webkit.WebViewFeature.isFeatureSupported
import com.hadiSormeyli.d3android.api.ChartApiDelegate
import com.hadiSormeyli.d3android.runtime.WebMessageChannel
import com.hadiSormeyli.d3android.runtime.controller.WebMessageController
import com.hadiSormeyli.d3android.runtime.messaging.LogLevel

@SuppressLint("RequiresFeature", "SetTextI18n")
class StockTreeMapVew @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    companion object {
        private const val DEFAULT_URL =
            "file:///android_asset/d3_treemap.html"
        private val features = listOf(
            CREATE_WEB_MESSAGE_CHANNEL,
            POST_WEB_MESSAGE,
            WEB_MESSAGE_PORT_SET_MESSAGE_CALLBACK,
            WEB_MESSAGE_PORT_POST_MESSAGE
        )
    }

    private val webView = WebSession(context, attrs, defStyleRes)

    var state: State = State.Preparing()
        private set(value) {
            field = value
            onStateChanged.forEach { it.invoke(value) }
        }


    private val logLevel = LogLevel.WARNING

    private var onStateChanged: MutableList<((State) -> Unit)> = mutableListOf()

    private val notSupportedFeatures: List<String> = checkSupportedFeature()

    private val channel: WebMessageChannel by lazy {
        WebViewCompat.createWebMessageChannel(webView)
            .asList()
            .let { WebMessageChannel(logLevel, it) }
            .apply { initConnection(webView) }
    }


    private val webMessageController = WebMessageController()

    val api by lazy { ChartApiDelegate(webMessageController) }

    private val loadingView = ProgressBar(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.CENTER
        }
        indeterminateTintList = ColorStateList.valueOf(Color.parseColor("#2B6CF7"))
        visibility = View.VISIBLE
    }


    init {
        webView.onSessionReady {
            state = when {
                notSupportedFeatures.isEmpty() -> {
                    webMessageController.setWebMessageChannel(channel)
                    loadingView.visibility = View.GONE
                    State.Ready()
                }

                else -> {
                    loadingView.visibility = View.GONE
                    val textView = TextView(context).apply {
                        layoutParams = LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT
                        ).apply {
                            gravity = Gravity.CENTER
                        }
                        visibility = View.VISIBLE
                        setTextColor(Color.GRAY)
                        text = "WebView does not support features: $notSupportedFeatures"
                    }
                    addView(textView)
                    State.Error(
                        Exception("WebView does not support features: $notSupportedFeatures")
                    )
                }
            }
        }
        addView(webView)
        addView(loadingView)
    }

    final override fun addView(child: View?) {
        super.addView(child)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (state is State.Ready) {
            loadingView.visibility = View.GONE
        } else {
            state = State.Preparing()
            webView.loadUrl(DEFAULT_URL)
        }
    }

    private fun checkSupportedFeature(): List<String> {
        return HashMap<String, Boolean>(features.size)
            .apply { features.forEach { this[it] = isFeatureSupported(it) } }
            .filter { !it.value }
            .keys.toList()
    }

    fun destroy() {
        webView.destroy()
        removeAllViews()
    }

    interface State {
        class Ready : State
        class Preparing : State
        class Error(val exception: Exception) : State
    }
}