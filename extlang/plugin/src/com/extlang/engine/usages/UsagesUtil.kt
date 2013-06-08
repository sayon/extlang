package com.extlang.engine.usages

import com.intellij.psi.PsiReference
import com.intellij.util.ProcessingContext
import com.intellij.psi.PsiElement
import com.extlang.engine.parser.ELASTNode
import com.extlang.engine.model.TokIdentifier
import com.intellij.openapi.util.TextRange
import java.util.ArrayList
import com.extlang.util.Util


class UsagesUtil
{
    class object {
        public fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
            if (element is ELASTNode)
            {
                val key = element.getName()
                val identifiers = Util.findIdentifiers(element.getProject(), key)
                val ret = identifiers.map {(node) -> ELReference(node.getPsi(), node.getTextRange()) }.toArray(array() : Array<PsiReference>)
                return ret
            }
            else
                return array()
        }
    }
}
