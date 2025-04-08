package com.hadiSormeyli.d3android.runtime.controller

import android.util.Log
import com.google.gson.JsonElement
import com.hadiSormeyli.d3android.api.serializer.Deserializer
import com.hadiSormeyli.d3android.api.serializer.PrimitiveSerializer
import com.hadiSormeyli.d3android.runtime.WebMessageChannel
import com.hadiSormeyli.d3android.runtime.messaging.BridgeFatalError
import com.hadiSormeyli.d3android.runtime.messaging.BridgeFunction
import com.hadiSormeyli.d3android.runtime.messaging.BridgeFunctionResult
import com.hadiSormeyli.d3android.runtime.messaging.BridgeMessage
import com.hadiSormeyli.d3android.runtime.messaging.BridgeSubscribeResult
import com.hadiSormeyli.d3android.runtime.messaging.BridgeSubscription
import com.hadiSormeyli.d3android.runtime.messaging.BridgeUnsubscribe
import com.hadiSormeyli.d3android.runtime.messaging.BridgeUnsubscribeResult
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedDeque

open class WebMessageController : WebMessageChannel.BridgeMessageListener {

    private var webMessageChannel: WebMessageChannel? = null
    private val callbackBuffer = ConcurrentHashMap<String, BufferElement>()
    private val messageBuffer = ConcurrentLinkedDeque<BridgeMessage>()

    fun callFunction(
        name: String,
        params: Map<String, Any> = emptyMap()
    ): String {
        return callBridgeFunction(name, params)
    }

    fun callFunction(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: (() -> Unit)?
    ): String {
        @Suppress("UNCHECKED_CAST")
        return callBridgeFunction(name, params, callback as? (Any?) -> Unit)
    }

    fun <T : Any?> callFunction(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: ((T) -> Unit)?,
        deserializer: Deserializer<out T>
    ): String {
        @Suppress("UNCHECKED_CAST")
        return callBridgeFunction(name, params, callback as? (Any?) -> Unit, deserializer)
    }

    private fun callBridgeFunction(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: ((Any?) -> Unit)? = null,
        deserializer: Deserializer<out Any?> = PrimitiveSerializer.NullDeserializer
    ): String {
        val bridge = BridgeFunction(name, params)

        @Suppress("UNCHECKED_CAST")
        callbackBuffer[bridge.uuid] = BufferElement(
            callback,
            deserializer,
            getStackTrace()
        )

        messageBuffer.addLast(bridge)
        sendMessages()
        return bridge.uuid
    }

    fun <T : Any> callSubscribe(
        name: String,
        params: Map<String, Any> = emptyMap(),
        callback: (T) -> Unit,
        deserializer: Deserializer<out T>
    ) {
        val bridge = BridgeSubscription(name, params)
        @Suppress("UNCHECKED_CAST")
        callbackBuffer[bridge.uuid] = BufferElement(
            callback as (Any?) -> Unit,
            deserializer,
            getStackTrace()
        )
        messageBuffer.addLast(bridge)
        sendMessages()
    }

    fun <T : Any> callUnsubscribe(
        name: String,
        subscription: (T) -> Unit
    ) {
        val uuid = callbackBuffer.filterValues { it.callback == subscription }.keys.firstOrNull()
        if (uuid != null) {
            callbackBuffer[uuid] = callbackBuffer[uuid]!!.makeInactive()
            val message = BridgeUnsubscribe(name, uuid)
            messageBuffer.addLast(message)
            sendMessages()
        }
    }

    override fun onMessage(bridgeMessage: BridgeMessage) {
        when (bridgeMessage) {
            is BridgeFunctionResult -> {
                val element = callbackBuffer.remove(bridgeMessage.uuid)
                    ?: run {

                        null
                    }
                element?.invoke(bridgeMessage.result)
            }

            is BridgeSubscribeResult -> {
                val element = callbackBuffer[bridgeMessage.uuid]
                if (element != null && !element.isInactive) {
                    element.invoke(bridgeMessage.result)
                }
            }

            is BridgeUnsubscribeResult -> {
                callbackBuffer.remove(bridgeMessage.uuid)
            }

            is BridgeFatalError -> {

            }
        }
    }

    private fun getStackTraceRegex(): Regex {
        val methodGroup = "(\\S+)"
        val fileGroup = "(file:[^:]+)"
        val lineGroup = "(\\d+)"
        val columnGroup = "(\\d+)"
        val pattern = "at\\s+$methodGroup\\s+[(]$fileGroup:$lineGroup:$columnGroup[)]"
        return Regex(pattern)
    }

    private fun getStackTrace(): Array<StackTraceElement> {
        return Thread.currentThread().stackTrace
            //remove current class name from the stacktrace
            .filter { it.className != WebMessageController::class.qualifiedName }
            //remove getCurrentThread and getStackTrace from the stacktrace
            .drop(2)
            .toTypedArray()
    }

    private fun sendMessages() {
        webMessageChannel?.apply {
            while (messageBuffer.isNotEmpty()) {
                messageBuffer.pollFirst()?.let(::sendMessage)
            }
        }
    }

    fun setWebMessageChannel(webMessageChannel: WebMessageChannel) {
        this.webMessageChannel = webMessageChannel
        webMessageChannel.setOnBridgeMessageListener(this)
        sendMessages()
    }

    data class BufferElement(
        val callback: ((Any?) -> Unit)? = null,
        val deserializer: Deserializer<out Any?>,
        val stackTrace: Array<StackTraceElement>,
        val isInactive: Boolean = false
    ) {
        fun invoke(jsonElement: JsonElement) {
            callback?.invoke(deserializer.deserialize(jsonElement))
        }

        fun makeInactive(): BufferElement = copy(isInactive = true)
    }
}

