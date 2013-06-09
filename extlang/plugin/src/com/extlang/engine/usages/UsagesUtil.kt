package com.extlang.engine.usages

import com.intellij.psi.PsiReference
import com.intellij.util.ProcessingContext
import com.intellij.psi.PsiElement
import com.extlang.engine.parser.ELASTNode
import com.extlang.engine.model.TokIdentifier
import com.intellij.openapi.util.TextRange
import java.util.ArrayList
import com.extlang.util.Util
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReferenceBase.Immediate
import com.intellij.openapi.project.Project


class UsagesUtil
{
    class object {


        public fun firstIdentifier(element: PsiNamedElement): PsiElement ?
        {
            val name = element.getName()
            val project = element.getProject()
            val identifiers = Util.findIdentifiers(project, name)
            return if (identifiers.size > 0) identifiers[0].getPsi() else null
        }
            public fun firstIdentifierReference(element: PsiNamedElement): PsiReference
        {
            val ident = firstIdentifier(element)
            val ref = ELReference(element, element.getTextRange(), ident!!)
            return ref
        }

    }
}
