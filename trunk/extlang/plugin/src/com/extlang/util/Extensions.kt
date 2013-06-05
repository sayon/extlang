package com.extlang.util

import java.util.ArrayList
import java.util.HashMap


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

        fun <T,U> Iterable<T>.cartesian(other: Iterable<U>): Iterable<Pair<T,U>> {
    val product = ArrayList<Pair<T,U>>()
    for (e1 in this) {
        for (e2 in other) {
            product.add(Pair(e1,e2))
        }
    }
    return product
}

fun <T> Iterable<T>.cartesianSquare() = this.cartesian(this)

class PairList <T, U>  : ArrayList<Pair<T,U>>()
{
    public fun addPair(fst: T, snd: U) : Boolean
    {
        return super.add(Pair(fst,snd))
    }
    public fun getValue(key: T)  : U ?
    {
        for (elem in this) {
            if (elem.first == key) return elem.second
        }
        return null
    }
}
