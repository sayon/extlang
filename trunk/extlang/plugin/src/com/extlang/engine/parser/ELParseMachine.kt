package com.extlang.engine.parser

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

import com.intellij.lang.PsiBuilder.Marker
import com.intellij.lang.PsiParser
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import java.util.Stack
import java.util.HashSet
import com.intellij.psi.text.BlockSupport
import com.intellij.lang.impl.PsiBuilderImpl
import java.util.ArrayList
import com.extlang.engine.parser.ELParseMachine
import com.extlang.engine.*

import com.extlang.parser.ELParserDefinition
import java.util.HashMap
import com.extlang.engine.parser.machine.Instruction
import com.extlang.engine.parser.machine.Program
import com.extlang.engine.parser.Entry.SymbolHolder


private abstract class Entry()
{
    class MarkerHolder(public val Mark: Marker?, public val TokenType: ELToken): Entry ()
    {
        public fun done()
        {
            if (Mark != null) Mark.done(TokenType)
        }
    }
    class SymbolHolder(public val Sym: Symbol, public val AliasedName: String? = null): Entry ()
    {

    }
    class QTreeHolder(public val Tree: QTree, public val Context: Map<String, Program>? = null): Entry()
}
class ELParseMachine(public val ParseTable: GrammarTable)
{

    inner class ParsingStack: Stack<Entry>()
    {
        public fun pushSymbol(s: Symbol)
        {
            push(Entry.SymbolHolder(s))
        }
        public fun pushRule(rule: Rule)
        {
            val trans = ParseTable.SyntaxProvided.Transformations!!
            if (rule.isExtension)
            {
                val aliases = trans.AliasesToIdx[rule]!!
                val idxs = trans.IdxToAliases[rule]!!
                for (i in (rule.size - 1) downTo 0)
                {
                    if (rule[i] == TermEpsilon.Instance) continue;

                    if  (idxs.containsKey(i))
                        push(Entry.SymbolHolder(rule[i], idxs[i]))
                    else
                        push(Entry.SymbolHolder(rule[i]))
                }
            }
            else
                for (elem  in rule.reverse().filter { s -> s != TermEpsilon.Instance }) {
                    push(Entry.SymbolHolder(elem))
                }
        }
        public fun pushMarker(marker: Marker?, symbol: NonTerminal): Entry =
                push(Entry.MarkerHolder(marker, ELToken.fromNonTerminal(symbol)))!!

        public override fun toString(): String
        {
            val sb = StringBuilder ()
            for (elem in this) {
                sb.append(" ")
                sb.append(elem)
            }
            return sb.toString()
        }
    }


    public fun parse(root: IElementType?, builder: PsiBuilder?): ASTNode
    {
        val m = builder!!.mark()!!
        val p = generateProgram(ParseTable.SyntaxProvided.Starter!!, builder)
        if (p != null)
        {
            executeProgram(p, builder, HashMap<String, Program>(), false)
        }
        else System.err.println("Empty program")

        while(!builder.eof())
            builder.advanceLexer()

        m.done(root)
        return builder.getTreeBuilt()!!
    }


    public fun executeProgram(program: Program,
                              builder: PsiBuilder,
                              environement: MutableMap<String, Program>,
                              buildPhantomTree: Boolean)
    {
        val stack = Stack<Pair<NonTerminal, Marker>>()
        fun PsiBuilder.insertTerminal(term: Terminal) {
            if (buildPhantomTree) this.mark()!!.done(ELToken.fromTerminal(term))
            else builder.advanceLexer()
        }
        fun PsiBuilder.closeNonTerminal() {
            val m = stack.pop()!!
            m.second.done(ELToken.fromNonTerminal(m.first))
        }
        for( instr in program)
            when (instr)
            {
                is Instruction.TerminalNode -> builder.insertTerminal(instr.Term)
                is Instruction.NonTerminalNodeOpen -> stack.push(Pair(instr.NonTerm, builder.mark()!!))
                is Instruction.NonTerminalNodeClose -> builder.closeNonTerminal()
                is Instruction.InsertTree -> executeProgram(environement[instr.AliasName]!!, builder, environement, instr.isPhantom)
                is Instruction.StoreNonTerminal -> {
                    environement.put(instr.AliasName, generateProgram(instr.NonTerm, builder))
                }

                else -> {
                    throw UnsupportedOperationException("instruction ${instr.repr()} is not yet supported")
                }
            }
    }


    public fun generateProgram(nt: NonTerminal, builder: PsiBuilder): Program? {
        val program = Program()
        val stack = ParsingStack()
        stack.pushSymbol(TermEndOfFile.Instance)
        stack.pushSymbol(nt)
        var error = false
        var currentPosition = 0

        fun advance()
        {
            currentPosition++
            while (ELParserDefinition.TOKENS_WHITE_SPACE.contains(builder.rawLookup(currentPosition)))
                currentPosition++
        }
        fun currentToken(): IElementType {
            val b = builder.rawLookup(currentPosition)
            if (b == null) ELToken.fromTerminal(TermEndOfFile.Instance)
            else b
        }
        fun applyRule(builder: PsiBuilder, ctoken: ELToken): Boolean
        {
            val sym = (stack.peek() as SymbolHolder).Sym
            val key = Pair(sym, ctoken.Term)
            if (!ParseTable.Table.containsKey(key))
            {
                System.err.println("No rule for ${key}")
                return false
            }
            val rules = ParseTable.Table[key]!!
            when (rules.size ){
                1 -> {
                    val top = (stack.peek() as SymbolHolder).Sym as NonTerminal
                    stack.pop()
                    stack.pushMarker(null, top)
                    program.add(Instruction.NonTerminalNodeOpen(top))

                    stack.pushRule(rules.head)
                    return true
                }
                else -> {
                    return false
                }
            }
        }
        while( !stack.isEmpty() && !error)
        {
            while( stack.peek() is Entry.MarkerHolder )
            {
                stack.pop()!!
                program.add(Instruction.NonTerminalNodeClose())
            }
            val ctoken = currentToken()
            val top = stack.peek()!!
            when (ctoken)
            {
                TokenType.BAD_CHARACTER -> return null
                is ELToken ->
                    {
                        if (top is SymbolHolder)
                            when  ((stack.peek() as SymbolHolder).Sym)
                            {
                                ctoken.Term -> {
                                    program.add(Instruction.TerminalNode(ctoken.Term)); stack.pop(); advance()
                                }
                                else ->
                                    {
                                        if (top.AliasedName != null)
                                            program.add(Instruction.StoreNonTerminal(top.Sym, top.AliasedName))
                                        error = !applyRule(builder, ctoken)
                                    }
                            }

                    }
                else -> {
                    builder.error("Bad element")
                    error = true
                }
            }
        }
        return if (error) null else program
    }

}
