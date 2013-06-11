package com.extlang.engine.model

import java.util.ArrayList
import sun.awt.Symbol
import com.extlang.engine.Symbol

/**This class represents right hand side of a grammar rule is a list of symbols with some additional functionality
*/
public class Rule(public val isExtension: Boolean = false): ArrayList<Symbol>()
{

    class object {
        var idCounter: Int = 0
    }

    val id : Int;

    {
        id = idCounter
        idCounter++
    }


    public override fun toString(): String =
        fold("", {(acc, elem) -> acc + elem.Name + " " })

    //Rules are equal iff all symbols correspond
    public fun equal(other: Rule): Boolean
    {
        if (size != other.size) return false
        for (i in 0..(size - 1) )
            if ( this[i].Name != other[i].Name ) return false
        return true
    }


    //The ID is used as a hashcode.
    public override fun hashCode(): Int
    {
        return id
    }

    val aliases = ArrayList<String?>()

    //Sets alias for a symbol at position idx
    public fun setAlias(idx: Int, name: String)
    {
        while (idx >= aliases.size) aliases.add(null)
        aliases[idx] = name
    }

    //add a new symbol
    public override fun add(e: Symbol): Boolean
    {
        aliases.add(null)
        return super.add(e)
    }

    //Returns true if a symbol at a specified position is named to include it in a quoted tree
    public fun isAlias (idx: Int): Boolean
    {
        return aliases[idx] != null
    }
    //Aliased name of symbol at position idx or null if symbol is not aliased
    public fun getAliasName(idx: Int): String?
    {
        return if (idx >= aliases.size) null
        else aliases[idx]
    }
}
