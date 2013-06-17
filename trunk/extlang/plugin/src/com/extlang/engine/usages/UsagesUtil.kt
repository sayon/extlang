package com.extlang.engine.usages

import com.intellij.psi.PsiReference
import com.intellij.psi.PsiElement
import com.extlang.util.Util
import com.intellij.psi.PsiNamedElement

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
        public fun firstIdentifierReference(element: PsiNamedElement): PsiReference?
        {
            val ident = firstIdentifier(element)
            return if (ident != null) ELReference(element, element.getTextRange()!!, ident.getParent()!!)
            else null
        }

    }
}
