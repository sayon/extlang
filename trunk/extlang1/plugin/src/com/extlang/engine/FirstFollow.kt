package com.extlang.engine

import com.extlang.util.cartesian
import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import com.extlang.engine.model.Rule

/**This class represents a pair of first nad follow sets, built after a grammar to create parsing table
*/
class FirstFollow (public val SyntaxProvided: AbstractSyntax)
{
    private var _first: HashMap<Symbol, HashSet<Terminal>> = HashMap<Symbol, HashSet<Terminal>>();
    private var _follow: HashMap<NonTerminal, HashSet<Terminal>> = HashMap<NonTerminal, HashSet<Terminal>>();

    {
        buildFirsts()
        buildFollows()
    }

    private fun buildFollows()
    {
        for( kvp in SyntaxProvided.NonTerminals)
            _follow.put(kvp.value, HashSet<Terminal>())

        //Always add eof to the starter
        _follow[ SyntaxProvided.Starter ]!!.add(TermEndOfFile.Instance)

        var size: Int
        do
        {
            size = _followsSize(_follow)
            _follow = _buildFollowIteration(_follow)

        }    while (size < _followsSize(_follow))
    }

    private fun _buildFollowIteration(follows: HashMap<NonTerminal, HashSet<Terminal>>): HashMap<NonTerminal, HashSet<Terminal>>
    {
        SyntaxProvided.forEachRule {(lhs, rule) ->
            //A -> aBb means First(b)\ Epsilon -> Follow(B)
            for ( i in 0..(rule.size - 1) )
                if (rule[i] is NonTerminal)
                    follows[ rule[i] ]!!.addAll(
                            first(rule.subList(i + 1, rule.size))
                                    .filter { t-> t != TermEpsilon.Instance }
                    )

            //A -> _ Bb if first(b) contains epsilon or A -> aB means Follow(A) -> Follow(B)
            for ( i in 0..rule.size - 1)
                if (rule[ i ] is NonTerminal &&
                first(rule.subList(i + 1, rule.size)).contains(TermEpsilon.Instance))
                    for ( item in follows[lhs]!! )
                        follows[rule[i]]!!.add(item)
            //A -> _ B
            if (rule.last is NonTerminal)
                follows[rule.last]!!.addAll(follows[lhs]!!)
        }
        return follows
    }

    public fun first(symbol: Symbol): HashSet<Terminal>?
    {
        return _first[symbol]
    }

    //Very ineffective!
    public fun first(symbols: Iterable<Symbol>): HashSet<Terminal>
    {
        val result = HashSet<Terminal>()
        var containsEpsilon = true
        for(sym in symbols)
        {
            if (!first(sym)!!.containsItem(TermEpsilon.Instance))
            {
                containsEpsilon = false
                result.addAll(first(sym)!!.filter {(s) -> s != TermEpsilon.Instance })
                break
            }
            else
                result.addAll(first(sym)!!)
        }
        if (containsEpsilon) result.add(TermEpsilon.Instance)

        return result
    }

    public fun follow(nt: NonTerminal): HashSet<Terminal>
    {
        return _follow[nt]!!
    }


    public fun representation(): String
    {
        val sb = StringBuilder()

        sb.append("--firsts:\n")
        for( kvp in _first)
        {
            if (kvp.key is Terminal) continue

            sb.append("${kvp.key.Name} -> ")
            for( v in kvp.value)
                sb.append("${v.Name}  ")

            sb.append("\n")

        }

        sb.append("--follows:\n")
        for( kvp in _follow)
        {
            sb.append("${kvp.key.Name} -> ")
            for( v in kvp.value)
                sb.append("${v.Name}  ")

            sb.append("\n")
        }
        return sb.toString()
    }

    private fun _firstsSize(firsts: HashMap<Symbol, HashSet<Terminal>>): Int
    {
        return firsts.values().fold(0, {(acc, elem) -> elem.size + acc });
    }
    private fun _followsSize(follows: HashMap<NonTerminal, HashSet<Terminal>>): Int
    {
        return follows.values().fold(0, {(acc, elem) -> elem.size + acc })
    }
    private fun buildFirsts()
    {
        for( t in SyntaxProvided.Terminals)
        {
            val initial = HashSet<Terminal>()
            initial.add(t.value)
            _first.put(t.value, initial)
        }
        for (nt in SyntaxProvided.NonTerminals)
            _first.put(nt.value, HashSet<Terminal>())

        var size: Int

        do
        {
            size = _firstsSize(_first)
            _first = _buildFirstIteration(_first)
        } while ( _firstsSize(_first) > size)
    }

    private fun _buildFirstIteration(firsts: HashMap<Symbol, HashSet<Terminal>>): HashMap<Symbol, HashSet<Terminal>>
    {
        for( kvp in firsts )
        {
            val symbol = kvp.key
            if (symbol is NonTerminal)
            {
                //if there is a rule X -> epsilon, add epsilon
                if (_producesEpsilon(symbol))
                    firsts[symbol]!!.add(TermEpsilon.Instance)

                //if there is a Production X -> Y1Y2..Yk then add first(Y1Y2..Yk) to first(X)
                //It is done recursively. If first(Y1) has no epsilon, it is first
                if (SyntaxProvided.Rules[symbol] != null)
                    for (rule in SyntaxProvided.Rules[symbol]!!)
                        if (rule.size == 0)
                            throw GrammarException("Empty rule")
                        else
                            _recursiveAddIntoFirst(symbol, rule, 0, firsts)

                //if all symbols on the rhs contain epsilon in their firsts, add epsilon
                if (_wholeRuleProducesEpsilon(symbol, firsts)) firsts[symbol]!!.add(TermEpsilon.Instance)
            }
            else continue
        }
        return firsts
    }
    private fun _producesEpsilon(sym: NonTerminal): Boolean
    {
        return SyntaxProvided.Rules.containsKey(sym) &&
        SyntaxProvided.Rules[sym]!!.any {(rule: Rule) ->
            rule.size == 1 &&
            rule[0] == TermEpsilon.Instance
        }
    }

    //A crappy name for a function, isn't it?
    private fun _wholeRuleProducesEpsilon(nt: NonTerminal,
                                          currentFirsts: HashMap<Symbol, HashSet<Terminal>>): Boolean
    {
        if (!SyntaxProvided.Rules.containsKey(nt)) return false
        return SyntaxProvided.Rules[nt]!!.any {(rule: Rule)->
            rule.all { sym ->
                currentFirsts[sym]!!.contains(TermEpsilon.Instance)
            }

        }
    }

    private fun _recursiveAddIntoFirst(nt: NonTerminal,
                                       rule: ArrayList<Symbol>,
                                       startIdx: Int,
                                       currentFirsts: HashMap<Symbol, HashSet<Terminal>>)
    {
        if (startIdx >= rule.size) return

        val currentSymbol = rule[startIdx]
        if (!currentFirsts[currentSymbol]!!.contains(TermEpsilon.Instance))
        {
            currentFirsts[nt]!!
                    .addAll(currentFirsts[currentSymbol]!!)
        }
        else {
            currentFirsts[nt]!!.addAll(
                    currentFirsts[currentSymbol]!!
                            .filter { t -> t != TermEpsilon.Instance }
            )
            _recursiveAddIntoFirst(nt, rule, startIdx + 1, currentFirsts)
        }
    }
}
