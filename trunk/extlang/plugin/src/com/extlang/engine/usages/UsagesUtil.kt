package com.extlang.engine.usages

import com.intellij.psi.PsiReference
import com.intellij.util.ProcessingContext
import com.intellij.psi.PsiElement
import com.extlang.engine.parser.ELASTNode
import com.extlang.engine.TokIdentifier
import com.intellij.openapi.util.TextRange


class UsagesUtil
{
    class object {

        public fun getReferencesByElement(element: PsiElement, context: ProcessingContext): Array<PsiReference> {
            if (element is ELASTNode )
            {
                val start = element.getNode().getStartOffset()
                val end = start + element.getNode().getTextLength() - 1
                return array(ELReference(element, TextRange(start, end)))
            }
            else
                return array()
        }
    }
}
