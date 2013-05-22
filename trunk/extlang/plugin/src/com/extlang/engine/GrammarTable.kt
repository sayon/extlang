package com.extlang.engine

import java.util.HashMap
import java.util.ArrayList
import com.extlang.extensions.cartesian


public class GrammarTable(public val SyntaxProvided: FixedSyntax)
{
    class object {
        public var Instance: GrammarTable? = null
    }
    public val FirstFollowSets: FirstFollow = FirstFollow(SyntaxProvided)   ;
    public val Table: HashMap<Pair<NonTerminal, Terminal>, ArrayList<Rule>> = _buildTable(SyntaxProvided, FirstFollowSets)

    public fun representation(): String {
        val sb = StringBuilder()
        sb.append("Table: \n ")
        for( (pair, rules) in Table )
        {
            if (rules.size == 0) continue

            sb.append("\n(${pair.first.Name},${pair.second.Name}}) -> \n")
            if (rules.size > 1)
                sb.append("Attention: more than one rule!\n")
            for (rule in rules)
            {
                val rulerepr = rule.fold("", {(acc, elem) -> acc + " " + elem.Name })
                sb.append("\t $rulerepr \n")
            }
        }
        sb.append("\n")
        return sb.toString()
    }

    private fun _buildTable(syntax: Syntax, ffsets: FirstFollow):
            HashMap<Pair<NonTerminal, Terminal>, ArrayList<Rule>>
    {
        System.err.println(FirstFollowSets.representation())
        val retval = HashMap<Pair<NonTerminal, Terminal>, ArrayList<Rule>>()

        for (pair in syntax.NonTerminals.values().cartesian(syntax.Terminals.values()))
            retval.put(pair, ArrayList<Rule>())

        SyntaxProvided.forEachRule {(A, rule) ->
            for ( first in ffsets.first(rule).filter {(x) -> x is Terminal } )
                retval[Pair(A, first)]!!._addUniqueRule(rule)

            if (ffsets.first(rule) .containsItem(TermEpsilon.Instance) )
            {
                for (b in ffsets.follow(A).filter {(x) -> x is Terminal })
                    retval[Pair(A, b)]!!._addUniqueRule(rule)
                if (ffsets.follow(A).containsItem(TermEndOfFile.Instance))
                    retval[Pair(A, TermEndOfFile.Instance)]!!._addUniqueRule(rule)
            }
        }
        return retval
    }
}

private fun ArrayList<Rule>._addUniqueRule(rule: Rule): Boolean
{
    if (this.containsItem(rule)) return false
    this.add(rule)
    return true
}