package com.extlang.util

public fun CharSequence.compareWith (startPosition: Int, seq: String): Boolean {
    var pos = startPosition
    for (element in seq)
        if (element != this[pos++])
            return false
    return true
}

public fun CharSequence.containsElement (value: Char): Boolean {
    for (element in this)
        if (element == value)
            return true
    return false
}