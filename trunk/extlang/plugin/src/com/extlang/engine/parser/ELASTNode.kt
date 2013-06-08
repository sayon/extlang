package com.extlang.engine.parser

import com.intellij.lang.ASTNode
import com.intellij.extapi.psi.ASTWrapperPsiElement
import java.util.HashSet
import com.extlang.engine.usages.ELNamedElement
import com.intellij.psi.PsiElement
import com.extlang.engine.model.ELToken
import com.extlang.engine.TermIdent
import com.extlang.engine.model.TokIdentifier
import com.intellij.psi.PsiNamedElement
      /*
public class ELASTNode(node: ASTNode): ASTWrapperPsiElement(node), PsiNamedElement
{
    public var Name: String = ""

    public override fun setName(name: String): PsiElement? {
        val elem = findChildByFilter(ELToken.AllIdentifiers())
        if (elem != null && elem is ELASTNode)
        {
            elem.Name = name
        }
        return this
    }

 /*   public override fun getName() :String?
    {
        val elem = findChildByFilter(ELToken.AllIdentifiers())
        if (elem != null && elem is ELASTNode && elem.getNode().getElementType() is TokIdentifier)
        {
            return elem.Name
        }
        return null
    }  */
}
        */