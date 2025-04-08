package com.hadiSormeyli.d3android.model

import com.google.gson.Gson

class TreeMapNodeData(
    val name: String,
    val value: Float? = null,
    val colorValue: Float? = null,
    val children: List<TreeMapNodeData>? = null
) {
    fun toJson() : String {
        return Gson().toJson(this)
    }
}