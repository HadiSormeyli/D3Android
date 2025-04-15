package com.hadiSormeyli.d3android.model

enum class RangeType {
    INCLUSIVE_INCLUSIVE,   // [min, max]
    INCLUSIVE_EXCLUSIVE,   // [min, max)
    EXCLUSIVE_INCLUSIVE,   // (min, max]
    EXCLUSIVE_EXCLUSIVE    // (min, max)
}