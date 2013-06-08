package com.extlang.engine

import java.util.ArrayList


/**QTree represents how a tree should be built when a specific extension  rule is applied.
The node can be a:
- reference to another node (alias for something we have parsed as a rule part
- terminal node  (usually leaf, but this is not restricted)
- nonterminal node
*/
open abstract class QTreeNode
{
    public abstract fun toString(): String
}

//The node can be a
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

class QTree (public var Root: QTreeNode? = null)