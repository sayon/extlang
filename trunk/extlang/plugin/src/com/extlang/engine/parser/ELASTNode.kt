package com.extlang.engine.parser

import com.intellij.lang.ASTNode
import com.intellij.extapi.psi.ASTWrapperPsiElement
import java.util.HashSet

class ELASTNode(node: ASTNode): ASTWrapperPsiElement(node)
{
    class object {
        val Nodes: HashSet<ELASTNode> = HashSet<ELASTNode>()
    }
    {
        Nodes.add(this)
    }

    {
        //System.err.println("Created element with ${ node.getElementType().toString()} text ${this.getText()}")
    }
}