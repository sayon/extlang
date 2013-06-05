package com.extlang.engine.parser.machine

import com.extlang.engine.NonTerminal
import com.extlang.engine.Terminal


abstract class Instruction
{
    class TerminalNode(public val Term: Terminal, public val Name:String? = null): Instruction() {
        public fun toString(): String =
                "term ${Term.Name}" + if (Name == null) "" else " name: $Name"
    }
    class NonTerminalNodeOpen(public val NonTerm: NonTerminal): Instruction() {
        public fun toString(): String =
                "nterm ${NonTerm.Name}"

    }
    class NonTerminalNodeClose(): Instruction() {
        public fun toString(): String =
                "ntclose"

    }
    class InsertTree(public val AliasName: String,
                     public val isPhantom: Boolean): Instruction(){
        public fun toString(): String =
                "Insert  $AliasName"
    }

    class PhantomOn(): Instruction(){
        public fun toString(): String =
                "Phantom On"

    }

    class PhantomOff(): Instruction(){
        public fun toString(): String =
                "Phantom Off"
    }

    class StoreNonTerminal(public val NonTerm: NonTerminal, public val AliasName: String): Instruction()
    {
        public fun toString(): String =
                "Begin $AliasName :: ${NonTerm.Name}"
    }
}