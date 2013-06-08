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
import com.extlang.engine.model.ELToken
import com.extlang.engine.model.TokIdentifier
import com.extlang.engine.model.Rule

/*
This classs is used to parse programs written on extensible language.
It can generate programs for the interpreting parser and execute them
**/
class ELParseMachine(public val ParseTable: GrammarTable)
{

    // This is a stack entry storing symbols or end markers
    private abstract class Entry()
    {
        class MarkerHolder(public val Mark: Marker?, public val TokenType: ELToken): Entry ()
        {
            public fun done()
            {
                if (Mark != null) Mark.done(TokenType)
            }
            public fun toString(): String
            {
                return "Marker ${TokenType.Sym}"
            }
        }
        class SymbolHolder(public val Sym: Symbol, public val AliasedName: String? = null): Entry ()
        {
            public fun toString(): String
            {
                return "${Sym.toString()}, alias=${AliasedName}"
            }
        }
        class QTreeHolder(public val Tree: QTree, public val Context: Map<String, Program>? = null): Entry()
        {
            public fun toString(): String
            {
                return "QTree${Tree.Root.toString()}"
            }
        }
    }

    //Stack used in parsing algorithm
    inner class ParsingStack: Stack<Entry>()
    {
        public fun pushSymbol(s: Symbol)
        {
            push(Entry.SymbolHolder(s))
        }
        public fun pushRule(rule: Rule)
        {
            if (rule.isExtension)
                for (i in (rule.size - 1) downTo 0)
                    if (rule[i] != TermEpsilon.Instance)
                        push(Entry.SymbolHolder(rule[i], rule.getAliasName(i)))
                    else
                        for (elem  in rule.reverse().filter { s -> s != TermEpsilon.Instance })
                            push(Entry.SymbolHolder(elem))
        }
        public fun pushMarker(marker: Marker?, symbol: NonTerminal): Entry =
                push(Entry.MarkerHolder(marker, ELToken.fromNonTerminal(symbol)))!!

        public override fun toString(): String
        {
            val sb = StringBuilder()
            for (elem in this)
                sb.append(" $elem")
            return sb.toString()
        }
    }

    /** Generates program to parse the whole file
    */
    public fun parse(root: IElementType?, builder: PsiBuilder?): ASTNode
    {
        val m = builder!!.mark()!!
        val p = generateProgram(ParseTable.SyntaxProvided.Starter!!, builder)
        if (p != null)
            executeProgram(p, builder, HashMap<String, Program>(), false)
        else System.err.println("Empty program")

        while(!builder.eof())
            builder.advanceLexer()
        m.done(root)
        return builder.getTreeBuilt()!!
    }

    /**Makes builder execute program in environment, defining how different programs are named.
    If phantomtree is true, the tree built will consume no tokens.*/
    //todo We can easily substitute environement for Stack<MutableMap> and store contextes.
    //todo This way we will guarantee correctness when using extensions inside extensions.
    public fun executeProgram(program: Program,
                              builder: PsiBuilder,
                              environment: MutableMap<String, Program>,
                              phantomTree: Boolean)
    {
        var phantomTreeSwitch = phantomTree
        val stack = Stack<Pair<NonTerminal, Marker>>()

        fun PsiBuilder.nonTerminalNodeClose()
        {
            val m = stack.pop()!!
            m.second.done(ELToken.fromNonTerminal(m.first))
        }
        for( instr in program)
            when (instr)
            {
                is Instruction.TerminalNode ->
                    // I wanted to use an inner function for that, but when I tried, Kotlin crashed, so...
                if (phantomTreeSwitch)
                {
                    if (instr.Term == TermIdent.Instance)
                    {
                        val tok = ELToken.fromIdentifier(instr.Name)
                        builder.mark()!!.done(tok)
                    }
                    else builder.mark()!!.done(ELToken.fromTerminal(instr.Term))
                }
                else
                    builder.advanceLexer()

                is Instruction.PhantomOn ->
                    phantomTreeSwitch = true
                is Instruction.PhantomOff ->
                    phantomTreeSwitch = false
                is Instruction.NonTerminalNodeOpen ->
                    stack.push(Pair(instr.NonTerm, builder.mark()!!))
                is Instruction.NonTerminalNodeClose ->
                    builder.nonTerminalNodeClose()
                is Instruction.InsertTree ->
                    executeProgram(environment[instr.AliasName]!!, builder, environment, instr.isPhantom)
                is Instruction.StoreNonTerminal ->
                    environment.put(instr.AliasName, generateProgram(instr.NonTerm, builder))

                else ->
                    throw UnsupportedOperationException("instruction ${instr.toString()} is not yet supported")

            }
    }

   /**Generates program to parse NonTerminal nt with PsiBuilder builder
   */
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
            val sym = (stack.peek() as Entry.SymbolHolder).Sym
            val key = Pair(sym, ctoken.Sym)
            if (!ParseTable.Table.containsKey(key))
            {
                System.err.println("No rule for ${key}")
                return false
            }
            val rules = ParseTable.Table[key]!!
            when (rules.size ){
                1 -> {
                    val top = (stack.peek() as Entry.SymbolHolder).Sym as NonTerminal
                    val rule = rules[0]
                    stack.pop()
                    stack.pushMarker(null, top)
                    if (rule.isExtension)
                        stack.push(Entry.QTreeHolder(ParseTable.SyntaxProvided.Transformations!!.QTrees[rule]!!))
                    program.add(Instruction.NonTerminalNodeOpen(top))

                    stack.pushRule(rule)
                    return true
                }
                else -> {
                    return false
                }
            }
        }
        while( stack.size > 1 && !error)
        {
            if ( stack.peek() is Entry.MarkerHolder )
            {
                stack.pop()!!
                program.add(Instruction.NonTerminalNodeClose())
                continue;
            }
            val ctoken = currentToken()
            val top = stack.peek()!!
            if (top is Entry.QTreeHolder)
            {
                val fromtree = Program.fromQTree(top.Tree)
                program.add(Instruction.PhantomOn())
                program.addAll(fromtree)
                program.add(Instruction.PhantomOff())
                stack.pop()
                continue;
            }

            when (ctoken)
            {
                TokenType.BAD_CHARACTER -> return null
                TokenType.WHITE_SPACE -> advance()
                is ELToken ->
                    {
                        if (top is Entry.SymbolHolder)
                            when  (top.Sym)
                            {
                                ctoken.Sym -> {

                                    val name = if (ctoken is TokIdentifier) ctoken.Name else null
                                    program.add(Instruction.TerminalNode(ctoken.Sym, name));

                                    stack.pop();
                                    advance()
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
