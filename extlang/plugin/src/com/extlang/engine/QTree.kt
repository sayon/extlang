package com.extlang.engine

import java.util.ArrayList

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
