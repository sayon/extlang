package com.extlang.engine

abstract class Symbol(public val Name: String)
{
    public fun toString() : String { return "[$Name]"}
}

class NonTerminal(name: String): Symbol(name)

open class Terminal(name: String): Symbol(name)
{
    public fun equal(other: ELToken) : Boolean
    {
        return other.Term == this
    }
}


class TermNumber(): Terminal("NUM")
{
   class object {
        public val Instance: TermNumber = TermNumber()
    }
}

class TermIdent(): Terminal("IDENT")
{
    class object {
        public val Instance: TermIdent = TermIdent()
    }
}
             /*
class TermStringLiteral(): Terminal("STRING")
{
   class object {
        public val Instance: TermStringLiteral = TermStringLiteral()
    }
}
      */
class TermEpsilon(): Terminal("EPSILON")
{
    class object {
        public val Instance: TermEpsilon = TermEpsilon()
    }
}
class TermEndOfFile(): Terminal("EOF")
{
    class object {
        public val Instance: TermEndOfFile = TermEndOfFile()
    }
}
