package com.extlang.engine

import java.util.ArrayList
import java.util.HashMap
import com.extlang.engine.model.Rule


/**Represents abstract syntax*/
public abstract class AbstractSyntax
{
    public var Starter: NonTerminal? = null
    public val Rules: Map<NonTerminal, ArrayList<Rule>> = HashMap<NonTerminal, ArrayList<Rule>> ()
    public val Terminals: Map<String, Terminal> = HashMap<String, Terminal> ()
    public val NonTerminals: Map<String, NonTerminal> = HashMap<String, NonTerminal>();


    public fun addTerminal(term: Terminal) : Terminal? =
        (Terminals as HashMap<String, Terminal>).put(term.Name, term)


    public fun addNonTerminal(nonterm: NonTerminal) : NonTerminal? =
        (NonTerminals as HashMap<String, NonTerminal>).put(nonterm.Name, nonterm)


    public fun addNonTerminalByName(name: String)
    {
        if (!NonTerminals.containsKey(name))
            (NonTerminals as HashMap<String, NonTerminal>).put(name, NonTerminal(name))
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

    // Perform action for each rule
    public fun forEachRule(f: (NonTerminal, Rule)-> Unit)
    {
        for( (lhs, rules) in Rules)
            for (rule in rules)
                f(lhs, rule)
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

}
