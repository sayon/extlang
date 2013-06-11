package com.extlang.engine

import com.extlang.grammar.psi.BNFSimpleTypes
import com.extlang.parser.BNFParserDefinition
import com.intellij.psi.impl.source.tree.TreeElement
import com.intellij.psi.tree.TokenSet
import java.util.ArrayList
import java.util.HashMap
import com.extlang.engine.model.ELToken
import com.extlang.engine.model.ExtendedSyntax
import com.extlang.engine.model.TreeTransformations
import com.intellij.openapi.diagnostic.Logger
import com.extlang.engine.model.Rule

private val TIdentifierTokenSet = TokenSet.create(BNFSimpleTypes.TIDENTIFIER);
private val NTIdentifierTokenSet = TokenSet.create(BNFSimpleTypes.NTIDENTIFIER);
private val NodeContentTokenSet = TokenSet.create(BNFSimpleTypes.NODECONTENT)
private val QuotedTreeTokenSet = TokenSet.create(BNFSimpleTypes.QUOTEDTREE)
private val AliasTokenSet = TokenSet.create(BNFSimpleTypes.ALIAS)
private val LHSTokenSet = TokenSet.create(BNFSimpleTypes.LEFTHANDSIDE)
private val RHSTokenSet = TokenSet.create(BNFSimpleTypes.RIGHTHANDSIDE)
private val TReprTokenSet = TokenSet.create(BNFSimpleTypes.TREPR)





/* This class is used to crawl through grammar definition's parsed tree.
It builds syntax, tree transformations,  and parsing table after them.
*/
public class GrammarBuilder(tree: TreeElement) {
    private val logger: Logger = Logger.getInstance("GrammarBuilder")!!
    //public val SyntaxBuilt: ExtendedSyntax ;
    //public val Transforms: TreeTransformations ;
    public val ParsingTable: GrammarTable;

    {
        val  SyntaxBuilt = ExtendedSyntax ()
        fillSyntax(SyntaxBuilt, tree)
        ELToken.reinitializeTokens()
        ParsingTable = GrammarTable(SyntaxBuilt)
        logger.info("Grammar file loaded")
    }


    //Fills syntax (and transformations)
    private fun fillSyntax(syntax: ExtendedSyntax, node: TreeElement)
    {
        val nodeName = node.getElementType().toString()
        when  (nodeName)
        {
            "TERMINALDESCRIPTION" ->
                addTerminal(syntax, node)
            "NONTERMINALS" ->
                addNonTerminals(syntax, node)
            "STARTER" ->
                addStarter(syntax, node)
            "RULE" ->
                addRule(syntax, node)
            else ->
                for( c in node.getChildren(null)!!)
                    fillSyntax(syntax, c as TreeElement)
        }
    }

    private fun addStarter(syntax: FixedSyntax, starterDescriptionNode: TreeElement)
    {
        val starterName = starterDescriptionNode.getChildren(NTIdentifierTokenSet)!![0].getText()
        syntax.Starter = syntax.NonTerminals[starterName]
    }
    private fun addTerminal(syntax: FixedSyntax, terminalDescriptionNode: TreeElement)
    {
        val term = terminalDescriptionNode.getChildren(TIdentifierTokenSet)!![0].getText()!!
        val repr = terminalDescriptionNode.getChildren(TReprTokenSet)!![0].getText()!!
        val reprNoQuotes = repr.trimLeading("\"").trimTrailing("\"")
        syntax.addTerminal(reprNoQuotes, term)
    }

    private fun addNonTerminals(syntax: FixedSyntax, nonTerminalsDescriptionNode: TreeElement)
    {
        for(elem in nonTerminalsDescriptionNode.getChildren(NTIdentifierTokenSet)!!)
            syntax.addNonTerminalByName(elem.getText()!!)
    }
    private fun addRule(syntax: ExtendedSyntax, ruleNode: TreeElement)
    {
        val lhs = ruleNode.getChildren(LHSTokenSet)!![0].getText()!!
        val rhs = ruleNode.getChildren(RHSTokenSet)!![0].getChildren(null)!!
        val quotedTreeNodeArr = ruleNode.getChildren(QuotedTreeTokenSet)!!
        val isExtension = quotedTreeNodeArr.size == 1
        val rhsPrepared = Rule(isExtension)
        var idx = -1
        for( elem in rhs)
        {
            if ( BNFParserDefinition.TOKENS_WHITE_SPACE.contains(elem.getElementType())) continue

            idx++

            val tidentOrDescr = elem.getChildren(null)!![0]
            val tidentOrDescrName = tidentOrDescr.getElementType().toString()
            when (tidentOrDescrName)
            {
                "TIDENTIFIER" ->
                    {
                        val symb = syntax.symbolByName(tidentOrDescr.getText()!!)
                        if (symb == null )
                            logger.error("Can't find terminal with the name of ${tidentOrDescr.getText()}")
                        else rhsPrepared.add(symb)
                    }
                "NTTERMINALORALIASED" ->
                    {
                        val symb = syntax.symbolByName(tidentOrDescr.getChildren(NTIdentifierTokenSet)!![0].getText()!!)
                        if (symb == null)
                            System.err.println("Can't find terminal with the name of ${tidentOrDescr.getText()}")
                        else {
                            val aliasarr = tidentOrDescr.getChildren(AliasTokenSet)!!
                            if (!aliasarr.isEmpty())
                                syntax.Transformations.addAlias(rhsPrepared, idx, aliasarr[0].getText()!!)
                            rhsPrepared.add(symb)
                        }
                    }
                else -> {
                }
            }

        }
        syntax.addRule(syntax.symbolByName(lhs) as NonTerminal, rhsPrepared)
        if (isExtension)
            addTree(quotedTreeNodeArr[0] as TreeElement, rhsPrepared, syntax)
    }

    private fun addTree(node: TreeElement, rule: Rule, syntax: ExtendedSyntax)
    {
        syntax.Transformations.QTrees.put(rule, QTree(buildTreeRecursive(node, syntax)))
    }

    //Recursively build tree building rule for a grammar rule
    private fun buildTreeRecursive(node: TreeElement, syntax: ExtendedSyntax): QTreeNode
    {
        val contents = node.getChildren(NodeContentTokenSet)!!
        val content = contents[0].getChildren(null)!![0]
        val children = node.getChildren(QuotedTreeTokenSet)!!
        val tree =
                when (content.getElementType().toString())
                {
                    "ALIAS" -> {
                        QTreeRefNode(content.getText()!!)
                    }
                    "NTIDENTIFIER" -> {
                        QTreeNonTermNode(syntax.symbolByName(content.getText()!!) as NonTerminal)
                    }
                    "TIDENTIFIER" -> {
                        QTreeTermNode(syntax.symbolByName(content.getText()!!)as NonTerminal)
                    }
                    else -> {
                        throw UnsupportedOperationException(
                                "This node type is not supported in the grammar: ${content.getElementType().toString()}")
                    }
                } :QTreeNode

        if (tree is QTreeNonTermNode)
            for (child in children)
                tree.Children.add(buildTreeRecursive(child as TreeElement, syntax))

        return tree
    }
}