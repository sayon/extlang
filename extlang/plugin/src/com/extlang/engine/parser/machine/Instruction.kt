package com.extlang.engine.parser.machine

import com.extlang.engine.NonTerminal
import com.extlang.engine.Terminal


abstract class Instruction
{
    class TerminalNode(public val Term: Terminal): Instruction()         {
        public fun toString(): String
        {
            return "term ${Term.Name}"
        }

    }
    class NonTerminalNodeOpen(public val NonTerm: NonTerminal): Instruction() {
        public fun toString(): String
        {
            return "nterm ${NonTerm.Name}"
        }
    }
    class NonTerminalNodeClose(): Instruction()                               {
        public fun toString(): String
        {
            return "ntclose"
        }
    }
    class InsertTree(public val AliasName: String,
                     public val isPhantom: Boolean): Instruction(){
        public fun toString(): String
        {
            return "Insert  $AliasName"
        }
    }

    class StoreNonTerminal(public val NonTerm: NonTerminal, public val AliasName: String): Instruction()
    {
        public fun toString(): String
        {
            return "Begin $AliasName :: ${NonTerm.Name}"
        }
    }
}