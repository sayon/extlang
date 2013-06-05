package com.extlang.engine.parser

import com.intellij.lang.ASTNode
import com.intellij.extapi.psi.ASTWrapperPsiElement
import java.util.HashSet
import com.extlang.engine.usages.ELNamedElement
import com.intellij.psi.PsiElement
import com.extlang.engine.ELToken
import com.extlang.engine.TermIdent

public class ELASTNode(node: ASTNode): ASTWrapperPsiElement(node), ELNamedElement
{
    var _name: String = ""
    public override fun setName(name: String): PsiElement? {
        _name = name // Maybe it should make a copy, should it?
        return this
    }
    public override fun getNameIdentifier(): PsiElement?
    {
        return findChildByType(ELToken.AllIdentifiers())
    }

}