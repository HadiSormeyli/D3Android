package com.hadiSormeyli.d3android.model

open class TreeMapNodeData(
    val name: String,
    val sizeValue: Float? = null,
    val colorValue: Float? = null,
    val value: Float? = sizeValue,
    val children: List<TreeMapNodeData>? = null
)