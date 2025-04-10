package com.hadiSormeyli.d3android.api

import com.google.gson.JsonElement
import com.hadiSormeyli.d3android.api.serializer.Deserializer
import com.hadiSormeyli.d3android.model.TreeMapNodeData
import com.hadiSormeyli.d3android.runtime.controller.WebMessageController
import com.hadiSormeyli.d3android.runtime.version.ChartRuntimeObject

class ChartApiDelegate(
    private val controller: WebMessageController
) : ChartRuntimeObject {
    override fun getVersion(): Int {
        return controller.hashCode()
    }

    fun setData(data: TreeMapNodeData) {
        controller.callFunction(
            "setData",
            mapOf("data" to data.toJson())
        )
    }

    fun onBackPressed(callback: (Boolean) -> Unit) {
        controller.callFunction(
            "onBackPressed",
            callback = callback,
            deserializer = object : Deserializer<Boolean>({}) {
                override fun deserialize(json: JsonElement): Boolean? {
                    return json.asBoolean
                }
            })
    }
}