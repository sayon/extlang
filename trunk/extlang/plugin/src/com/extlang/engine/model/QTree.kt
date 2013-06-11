package com.extlang.engine

import java.util.ArrayList


//This is a class that incapsulates a tree inside it and can hold additional information about it.
class QTree (public var Root: QTreeNode? = null)

/**QTree represents how a tree should be built when a specific extension rule is applied.
The node can be a:
- reference to another node (alias for something we have parsed as a rule part
- terminal node  (usually leaf, but this is not restricted)
- nonterminal node
*/
open abstract class QTreeNode
{
    public abstract fun toString(): String
}

//The node can be a reference to a named tree
class QTreeRefNode(public val RefName: String): QTreeNode()
{
    public override fun toString(): String {
        return "<ref: ${RefName}>"
    }
}


//The node can be a terminal
class QTreeTermNode(public val Term: Terminal): QTreeNode()
{
    public override fun toString(): String
    {
        return "<${Term.Name}>"
    }
}


//The node can be a non-terminal
class QTreeNonTermNode(public val NonTerm: NonTerminal): QTreeNode()
{
    public override fun toString(): String {
        return "<${Children.fold("", {(acc, elem) -> acc + " " + elem })}>"
    }
    public val Children: ArrayList<QTreeNode> = ArrayList<QTreeNode>()
}
