package com.hadiSormeyli.d3android

import android.util.Log
import android.webkit.JavascriptInterface

class WebAppInterface() {

    @JavascriptInterface
    fun onNodeClicked(name: String) {
        Log.d("WebAppInterface", "Node clicked: $name")
    }

    @JavascriptInterface
    fun onBackPressedJS() {
        Log.d("WebAppInterface", "Back button pressed")
    }
}
