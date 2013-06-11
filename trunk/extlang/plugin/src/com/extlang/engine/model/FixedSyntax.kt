package com.extlang.engine

import java.util.HashMap


/**When the syntax is fixed, all terminals have their representations fixed.*/
public open class FixedSyntax: AbstractSyntax()
{
    //maps text representation to terminal instance
    public val Reprs: HashMap<String, Terminal> = HashMap<String, Terminal>() ;
    public val TerminalsToReprs: HashMap<Terminal, String> = HashMap<Terminal, String>();

    //ctor
    {
        //These terminals are present in any fixed syntax.
        addTerminal(TermIdent.Instance)
        addTerminal(TermNumber.Instance)
        addTerminal(TermEpsilon.Instance)
        addTerminal(TermEndOfFile.Instance)
    }

    //The right way to add a terminal together with its representation
    public fun addTerminal(repr: String, name: String)
    {
        val term = Terminal(name)
        Reprs.put(repr, term)
        TerminalsToReprs.put(term,repr)
        super.addTerminal(term)
    }

}
