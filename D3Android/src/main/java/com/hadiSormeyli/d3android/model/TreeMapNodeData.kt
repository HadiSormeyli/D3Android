package com.hadiSormeyli.d3android.model

open class TreeMapNodeData(
    val name: String,
    val value: Float? = null,
    val colorValue: Float? = null,
    val labelValue: Float? = value,
    val children: List<TreeMapNodeData>? = null
)