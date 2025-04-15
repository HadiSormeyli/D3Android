package com.hadiSormeyli.d3android.model

class ColorRange(
    val color: String,
    val max: Float,
    val min: Float,
    val rangeType: RangeType = RangeType.INCLUSIVE_EXCLUSIVE
)