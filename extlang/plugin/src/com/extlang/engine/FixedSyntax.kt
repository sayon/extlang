package com.extlang.engine

import java.util.HashMap

public class FixedSyntax: Syntax()
{
    public val Reprs: HashMap<String, Terminal> = HashMap<String, Terminal>() ;


    {
        addTerminal(TermIdent.Instance)
        addTerminal(TermNumber.Instance)
        addTerminal(TermEpsilon.Instance)
        addTerminal(TermEndOfFile.Instance)
    }

    class object {
        public var Instance: FixedSyntax = FixedSyntax()
    }

    public fun addTerminal(repr: String, name: String)
    {
        val term = Terminal(name)
        Reprs.put(repr, term)
        super.addTerminal(term)
    }
}