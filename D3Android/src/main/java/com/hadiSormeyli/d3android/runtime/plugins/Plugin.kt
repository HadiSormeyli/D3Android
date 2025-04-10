package com.hadiSormeyli.d3android.runtime.plugins

import java.util.*

open class Plugin(
        val uuid: String = UUID.randomUUID().toString(),
        val name: String,
        val file: String,
        val configurationParams: Any? = null
)