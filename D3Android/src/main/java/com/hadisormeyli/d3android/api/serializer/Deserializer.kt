package com.hadiSormeyli.d3android.api.serializer

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.hadiSormeyli.d3android.api.serializer.gson.GsonProvider


abstract class Deserializer<T>(function: () -> Unit) {

    protected open val gson: Gson by lazy { GsonProvider.newInstance() }

    abstract fun deserialize(json: JsonElement): T?
}