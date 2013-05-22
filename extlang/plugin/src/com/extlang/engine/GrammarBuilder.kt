package com.extlang.engine

import com.extlang.grammar.psi.BNFSimpleTypes
import com.extlang.parser.BNFParserDefinition
import com.intellij.psi.impl.source.tree.TreeElement
import com.intellij.psi.tree.TokenSet
import java.util.ArrayList
import java.util.HashMap

val TIdentifierTokenSet = TokenSet.create(BNFSimpleTypes.TIDENTIFIER);
val NTIdentifierTokenSet = TokenSet.create(BNFSimpleTypes.NTIDENTIFIER);
val NodeContentTokenSet = TokenSet.create(BNFSimpleTypes.NODECONTENT)
val QuotedTreeTokenSet = TokenSet.create(BNFSimpleTypes.QUOTEDTREE)
val AliasTokenSet = TokenSet.create(BNFSimpleTypes.ALIAS)
val LHSTokenSet = TokenSet.create(BNFSimpleTypes.LEFTHANDSIDE)
val RHSTokenSet = TokenSet.create(BNFSimpleTypes.RIGHTHANDSIDE)
val TReprTokenSet = TokenSet.create(BNFSimpleTypes.TREPR)

/** Once the grammar file is edited, the grammar inner representation must be rebuilt.
This is atm done through creating instance of GrammarBuilder inside GrammarAction and changing
FixedSyntax.Instance so that it corresponds to the new grammar.
todo consider making a unified event-driven crawler for such trees.
 todo It can be of a great use when doing tree transformations, can it?
 decisionmaker, map something it returns -> bool, for each source make a callback
*/

open abstract class QTreeNode
{

    public abstract fun toString(): String
}

class QTreeRefNode(public val RefName: String): QTreeNode()
{
    public override fun toString(): String {
        return "<ref: ${RefName}>"
    }
}
class QTreeTermNode(public val Term: Terminal): QTreeNode()
{
    public override fun toString(): String
    {
        return "<${Term.Name}>"
    }
}

class QTreeNonTermNode(public val NonTerm: NonTerminal): QTreeNode()
{
    public override fun toString(): String {
        return "<${Children.fold("", {(acc, elem) -> acc + " " + elem })}>"
    }
    public val Children: ArrayList<QTreeNode> = ArrayList<QTreeNode>()
}


class QTree
{
    public var Root: QTreeNode? = null

}

 class TreeTransformations
{
    public val QTrees: HashMap<Rule, QTree> = HashMap<Rule, QTree>()
    public val Aliases: HashMap<
            Rule,
            HashMap<String, Pair<Rule, Int>>
            > = HashMap<Rule, HashMap<String, Pair<Rule, Int>>>()
    public fun addAlias(rule: Rule, idx: Int, alias: String)
    {
        if (!Aliases.containsKey(rule))
            Aliases.put(rule, HashMap<String, Pair<Rule, Int>>())
        Aliases[rule]!!.put(alias, Pair(rule, idx))
    }

}
public class GrammarBuilder(tree: TreeElement) {
    public val SyntaxBuilt: FixedSyntax ;
    public val Transforms: TreeTransformations ;
    public val ParsingTable: GrammarTable;

    {
        SyntaxBuilt  = FixedSyntax()
        Transforms = TreeTransformations()
        fillSyntax(SyntaxBuilt, tree)
        ELToken.reinitializeTokens()
        ParsingTable = GrammarTable(SyntaxBuilt)

     /*   for (tree in Transforms!!.QTrees.values())
            System.err.println(tree.toString())*/
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
        val rhsPrepared = Rule()
        var idx = -1
        for( elem in rhs)
        {
            idx++
            if ( !BNFParserDefinition.TOKENS_WHITE_SPACE.contains(elem.getElementType()))
            {
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
        }
        syntax.addRule(syntax.symbolByName(lhs), rhsPrepared)
        val quotedTreeNodeArr = ruleNode.getChildren(QuotedTreeTokenSet)!!
        if (quotedTreeNodeArr.size == 1)
            addTree(Transforms, quotedTreeNodeArr[0] as TreeElement, rhsPrepared, syntax.symbolByName(lhs) as NonTerminal)
    }

    private fun addTree(trans: TreeTransformations, node: TreeElement, rule: Rule, nt: NonTerminal)
    {
        Transforms.QTrees.put(rule, buildTreeRecursive(node))

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