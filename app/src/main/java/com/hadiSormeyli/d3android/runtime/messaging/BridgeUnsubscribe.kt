package com.hadiSormeyli.d3android.runtime.messaging

class BridgeUnsubscribe(
    functionName: String,
    uuid: String
) : BridgeMessage(
    MessageType.UNSUBSCRIBE,
    Data(
        uuid = uuid,
        fn = functionName
    )
)