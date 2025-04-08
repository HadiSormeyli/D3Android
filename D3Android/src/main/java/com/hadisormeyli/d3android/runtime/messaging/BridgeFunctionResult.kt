package com.hadiSormeyli.d3android.runtime.messaging

import com.google.gson.JsonElement
import com.google.gson.JsonNull

class BridgeFunctionResult(
    bridgeMessage: BridgeMessage
) : BridgeMessage(
    bridgeMessage.messageType,
    bridgeMessage.data
) {
    val uuid: String get() = data.uuid
    val result: JsonElement get() = data.result ?: JsonNull.INSTANCE
}