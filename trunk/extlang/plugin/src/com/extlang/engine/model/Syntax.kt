package com.extlang.engine

import java.util.ArrayList
import java.util.HashMap

public class Rule(public val isExtension: Boolean = false): ArrayList<Symbol>()
{
    {
        counter++
    }
    public override fun toString(): String
    {
        return fold("", {(acc, elem) -> acc + elem.Name + " " })
    }

    public fun equal(other: Rule): Boolean
    {
        if (size != other.size) return false
        for (i in 0..(size - 1) )
            if ( this[i].Name != other[i].Name ) return false
        return true
    }

    class object {
        var counter: Int = 0
    }
    public override fun hashCode(): Int
    {
        return counter
    }
    val aliases = ArrayList<String?>()

    public fun addAlias(s: Symbol, name: String)
    {
        aliases.add(name)
        super.add(s)
    }

    public fun setAlias(idx: Int, name:String)
    {
        while (idx >= aliases.size) aliases.add(null)
        aliases[idx] = name
    }
    public override fun add(s: Symbol): Boolean
    {
        aliases.add(null)
        return super.add(s)
    }
    public fun isAlias (idx: Int): Boolean
    {
        return aliases[idx] != null
    }
    public fun getAliasName(idx: Int): String?
    {
        return if (idx >= aliases.size) null
        else aliases[idx]
    }
}

public abstract class Syntax
{
    public var Starter: NonTerminal? = null
    public val Rules: Map<NonTerminal, ArrayList<Rule>> = HashMap<NonTerminal, ArrayList<Rule>> ()
    public val Terminals: Map<String, Terminal> = HashMap<String, Terminal> ()
    public val NonTerminals: Map<String, NonTerminal> = HashMap<String, NonTerminal>();

    public fun addTerminal(term: Terminal)
    {
        (Terminals as HashMap<String, Terminal>).put(term.Name, term)
    }
    public fun addNonTerminal(nonterm: NonTerminal)
    {
        (NonTerminals as HashMap<String, NonTerminal>).put(nonterm.Name, nonterm)
    }

    public fun addNonTerminalByName(name: String)
    {
        if (!NonTerminals.containsKey(name))
        {
            (NonTerminals as HashMap<String, NonTerminal>).put(name, NonTerminal(name))
        }
    }

    public fun symbolByName(name: String): Symbol?
    {
        val termcandidate = Terminals.get(name)
        if (termcandidate == null) return NonTerminals.get(name)
        else return termcandidate
    }

    public fun addRule(nt: NonTerminal, row: Rule)
    {
        if (Rules[nt] == null)
            (Rules as HashMap<NonTerminal, ArrayList<Rule>>).put(nt, ArrayList<Rule>())
        Rules[nt]!!.add(row);
    }

    public fun representation(): String
    {
        val sb = StringBuilder()
        sb.append("--nonterminals:\n")
        for( kvp in NonTerminals)
            sb.append("${kvp.component2().Name}\n")

        sb.append("--terminals:\n")
        for( kvp in Terminals )
            sb.append("${kvp.component2().Name}\n")

        sb.append("--starter: ${if (Starter == null) "null" else Starter!!.Name}\n")
        sb.append("--rules:\n")
        forEachRule {(nt, rule) ->
            sb.append ("${nt.Name} ::= ${rule} \n")
        }

        return sb.toString()
    }

    public fun forEachRule(f: (NonTerminal, Rule)-> Unit)
    {
        for( (lhs, rules) in Rules)
            for (rule in rules)
                f(lhs, rule)
    }
}
