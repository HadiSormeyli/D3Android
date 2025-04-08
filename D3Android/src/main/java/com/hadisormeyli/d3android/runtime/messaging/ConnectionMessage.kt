package com.hadiSormeyli.d3android.runtime.messaging

import java.util.*

class ConnectionMessage(logLevel: LogLevel): BridgeMessage(
    MessageType.CONNECTION,
    Data(
        uuid = UUID.randomUUID().toString(),
        logLevel = logLevel
    )
)