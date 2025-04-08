package com.hadiSormeyli.d3android.runtime

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import androidx.webkit.WebMessageCompat
import androidx.webkit.WebMessagePortCompat
import androidx.webkit.WebViewCompat
import com.hadiSormeyli.d3android.api.serializer.gson.GsonProvider
import com.hadiSormeyli.d3android.runtime.messaging.BridgeFatalError
import com.hadiSormeyli.d3android.runtime.messaging.BridgeFunctionResult
import com.hadiSormeyli.d3android.runtime.messaging.BridgeMessage
import com.hadiSormeyli.d3android.runtime.messaging.BridgeSubscribeResult
import com.hadiSormeyli.d3android.runtime.messaging.BridgeUnsubscribeResult
import com.hadiSormeyli.d3android.runtime.messaging.ConnectionMessage
import com.hadiSormeyli.d3android.runtime.messaging.LogLevel
import com.hadiSormeyli.d3android.runtime.messaging.MessageType

@SuppressLint("RequiresFeature")
class WebMessageChannel(private val logLevel: LogLevel, ports: List<WebMessagePortCompat>) {
    private val serializer = GsonProvider.newInstance()

    private val jsPort = ports[1]
    private val 
            nativePort = ports[0]
    private var onBridgeMessageListener: BridgeMessageListener? = null

    init {
        nativePort.setWebMessageCallback(object : WebMessagePortCompat.WebMessageCallbackCompat() {
            override fun onMessage(port: WebMessagePortCompat, webMessage: WebMessageCompat?) {
                if (webMessage != null) {
                    val bridgeMessage = bridgeMessageOf(webMessage)
                    onBridgeMessageListener?.onMessage(bridgeMessage)
                }
            }
        })
    }

    fun initConnection(webView: WebView) {
        WebViewCompat.postWebMessage(
            webView,
            webMessageOf(ConnectionMessage(logLevel), jsPort),
            Uri.EMPTY
        )
    }

    fun sendMessage(bridgeMessage: BridgeMessage) {
        nativePort.postMessage(webMessageOf(bridgeMessage))
    }

    fun setOnBridgeMessageListener(listener: BridgeMessageListener?) {
        onBridgeMessageListener = listener
    }

    private fun webMessageOf(
        bridgeMessage: BridgeMessage,
        port: WebMessagePortCompat? = null
    ): WebMessageCompat {
        val jsonMessage = serializer.toJson(bridgeMessage)

        return WebMessageCompat(jsonMessage, port?.let { arrayOf(it) })
    }

    private fun bridgeMessageOf(webMessage: WebMessageCompat): BridgeMessage {
        val message = serializer.fromJson(
            webMessage.data,
            BridgeMessage::class.java
        )

        return when (message.messageType) {
            MessageType.FUNCTION_RESULT -> BridgeFunctionResult(message)
            MessageType.SUBSCRIBE_RESULT -> BridgeSubscribeResult(message)
            MessageType.UNSUBSCRIBE_RESULT -> BridgeUnsubscribeResult(message)
            MessageType.FATAL_ERROR -> BridgeFatalError(message)
            else -> message
        }
    }

    interface BridgeMessageListener {
        fun onMessage(bridgeMessage: BridgeMessage)
    }
}
