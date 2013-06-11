package com.extlang.engine.parser.machine

import com.extlang.engine.NonTerminal
import com.extlang.engine.Terminal

/**This is an instruction for interpreting parser. A Program class implements a list of instructions
*/
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
    //Insert a tree from environement
    class InsertTree(public val AliasName: String,
                     public val isPhantom: Boolean): Instruction(){
        public fun toString(): String =
                "Insert  $AliasName"
    }

    //When this instruction is executed, the following terminal insertions won't consume any tokens
    class PhantomOn(): Instruction(){
        public fun toString(): String =
                "Phantom On"

    }
    //When this instruction is executed, the following terminal insertions will consume tokens
    class PhantomOff(): Instruction(){
        public fun toString(): String =
                "Phantom Off"
    }

    /**
    Generate a program to parse a nonterminal at current position, store it in environement as AliasName
    and then parse it for real.*/
    class StoreNonTerminal(public val NonTerm: NonTerminal, public val AliasName: String): Instruction()
    {
        public fun toString(): String =
                "Begin $AliasName :: ${NonTerm.Name}"
    }
}