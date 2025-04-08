package com.hadiSormeyli.d3android.api.serializer.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonProvider {
    fun newInstance(): Gson {
        return GsonBuilder()
            .registerDefaultAdapters()
            .create()
    }
}

fun GsonBuilder.registerDefaultAdapters(): GsonBuilder {
    return this
}