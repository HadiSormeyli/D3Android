package com.hadiSormeyli.d3android.model

class TreeColorOptions(
    val headerColor: String = "#4A4D57",
    val colorRanges: List<ColorRange> = listOf(
        ColorRange(
            color = "#F71E3C",
            min = -Float.MAX_VALUE,
            max = -3f,
        ),
        ColorRange(
            color = "#B53849",
            min = -3f,
            max = -1f
        ),
        ColorRange(
            color = "#414554",
            min = -1f,
            max = 1f
        ),
        ColorRange(
            color = "#318F54",
            min = 1f,
            max = 3f
        ),
        ColorRange(
            color = "#26C963",
            min = 3f,
            max = Float.MAX_VALUE
        )
    )
)