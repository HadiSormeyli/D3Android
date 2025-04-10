package com.hadiSormeyli.d3android.runtime.plugins


class Eval(val function: String) : Plugin(
    name = "evalPlugin",
    file = "scripts/plugins/eval-plugin/main.js",
    configurationParams = EvalParams(function)
)

data class EvalParams(
    val f: String
)