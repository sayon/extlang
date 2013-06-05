package com.extlang.engine

import com.extlang.grammar.psi.BNFSimpleTypes
import com.extlang.parser.BNFParserDefinition
import com.intellij.psi.impl.source.tree.TreeElement
import com.intellij.psi.tree.TokenSet
import java.util.ArrayList
import java.util.HashMap
import com.extlang.engine.model.ExtendedSyntax

val TIdentifierTokenSet = TokenSet.create(BNFSimpleTypes.TIDENTIFIER);
val NTIdentifierTokenSet = TokenSet.create(BNFSimpleTypes.NTIDENTIFIER);
val NodeContentTokenSet = TokenSet.create(BNFSimpleTypes.NODECONTENT)
val QuotedTreeTokenSet = TokenSet.create(BNFSimpleTypes.QUOTEDTREE)
val AliasTokenSet = TokenSet.create(BNFSimpleTypes.ALIAS)
val LHSTokenSet = TokenSet.create(BNFSimpleTypes.LEFTHANDSIDE)
val RHSTokenSet = TokenSet.create(BNFSimpleTypes.RIGHTHANDSIDE)
val TReprTokenSet = TokenSet.create(BNFSimpleTypes.TREPR)



public class TreeTransformations
{
    public val QTrees: HashMap<Rule, QTree> = HashMap<Rule, QTree>()
    public val AliasesToIdx: HashMap<
            Rule,
            HashMap<String, Int>
            > = HashMap<Rule, HashMap<String, Int>>()
    public val IdxToAliases: HashMap<
            Rule,
            HashMap<Int, String>
            > = HashMap<Rule, HashMap<Int, String>>()
    public fun addAlias(rule: Rule, idx: Int, alias: String)
    {
        if (!AliasesToIdx.containsKey(rule))
            AliasesToIdx.put(rule, HashMap<String, Int>())
        if (!IdxToAliases.containsKey(rule))
            IdxToAliases.put(rule, HashMap<Int, String>())

        AliasesToIdx[rule]!!.put(alias, idx)
        IdxToAliases[rule]!!.put(idx, alias)
        rule.setAlias(idx, alias)
    }

}
public class GrammarBuilder(tree: TreeElement) {
    public val SyntaxBuilt: ExtendedSyntax ;
    public val Transforms: TreeTransformations ;
    public val ParsingTable: GrammarTable;

    {
        SyntaxBuilt = ExtendedSyntax ()
        Transforms = TreeTransformations()
        fillSyntax(SyntaxBuilt, tree)
        ELToken.reinitializeTokens()
        ParsingTable = GrammarTable(SyntaxBuilt)
        SyntaxBuilt.Transformations = Transforms
    }


    private fun fillSyntax(syntax: FixedSyntax, node: TreeElement)
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
            syntax.addNonTerminalByName(elem.getText())
    }
    private fun addRule(syntax: FixedSyntax, ruleNode: TreeElement)
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
                        val symb = syntax.symbolByName(tidentOrDescr.getText())
                        if (symb == null) System.err.println("Can't find terminal with the name of ${tidentOrDescr.getText()}")
                        else rhsPrepared.add(symb)
                    }
                "NTTERMINALORALIASED" ->
                    {
                        val symb = syntax.symbolByName(tidentOrDescr.getChildren(NTIdentifierTokenSet)!![0].getText())
                        if (symb == null)
                            System.err.println("Can't find terminal with the name of ${tidentOrDescr.getText()}")
                        else {

                            val aliasarr = tidentOrDescr.getChildren(AliasTokenSet)!!
                            if (!aliasarr.isEmpty())
                                Transforms.addAlias(rhsPrepared, idx, aliasarr[0].getText())
                            rhsPrepared.add(symb)
                        }
                    }
                else -> {
                }
            }

        }
        syntax.addRule(syntax.symbolByName(lhs), rhsPrepared)
        if (isExtension)
            addTree(quotedTreeNodeArr[0] as TreeElement, rhsPrepared)
    }

    private fun addTree(node: TreeElement, rule: Rule)
    {
        Transforms.QTrees.put(rule, QTree(buildTreeRecursive(node)))
    }
    private fun buildTreeRecursive(node: TreeElement): QTreeNode
    {
        val contents = node.getChildren(NodeContentTokenSet)!!
        val content = contents[0].getChildren(null)!![0]
        val children = node.getChildren(QuotedTreeTokenSet)!!
        val tree =
                when (content.getElementType().toString())
                {
                    "ALIAS" -> {
                        QTreeRefNode(content.getText())
                    }
                    "NTIDENTIFIER" -> {
                        QTreeNonTermNode(SyntaxBuilt.symbolByName(content.getText()))
                    }
                    "TIDENTIFIER" -> {
                        QTreeTermNode(SyntaxBuilt.symbolByName(content.getText()))
                    }
                    else -> {
                        throw UnsupportedOperationException(
                                "This node type is not supported in the grammar: ${content.getElementType().toString()}")
                    }
                } :QTreeNode

        if (tree is QTreeNonTermNode)
            for (child in children)
                tree.Children.add(buildTreeRecursive(child as TreeElement))

        return tree
    }
}