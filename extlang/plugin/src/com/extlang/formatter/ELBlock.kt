package com.extlang.formatter

import com.intellij.psi.formatter.common.AbstractBlock
import com.intellij.formatting.Block
import com.intellij.formatting.Spacing
import com.intellij.lang.ASTNode
import com.intellij.formatting.Wrap
import com.intellij.formatting.SpacingBuilder
import com.intellij.formatting.Alignment
import com.intellij.formatting.Indent
import com.intellij.formatting.WrapType
import java.util.ArrayList
import com.intellij.diagnostic.LogMessageEx
import com.extlang.util.Util

public class ELBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, public val spacingBuilder: SpacingBuilder)
: AbstractBlock(node, wrap, alignment)
{

    public override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return spacingBuilder.getSpacing(this, child1, child2)
    }
    public override fun isLeaf(): Boolean {
        return myNode.getFirstChildNode() == null
    }
    protected override fun buildChildren(): MutableList<Block>? {
        val res = ArrayList<Block>()
        val ali = myAlignment
        val spa = spacingBuilder
        if (myNode.getChildren(null)!!.size == 0) return res
        val leaves = Util.CollectLeaves(myNode)
        for( child in leaves)
        {
            val block = ELBlock(child, Wrap.createWrap(WrapType.CHOP_DOWN_IF_LONG, true), ali, spa);
            res.add(block)
        }
        return res
    }

    public override fun getIndent(): Indent
    {
        return Indent.getNoneIndent()!!
    }
}

