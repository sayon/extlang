package com.extlang.annotator

import com.intellij.lang.annotation.Annotator
import com.intellij.psi.PsiElement
import com.intellij.lang.annotation.AnnotationHolder
import com.extlang.engine.model.TokIdentifier
import com.extlang.engine.model.ELToken
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.editor.SyntaxHighlighterColors
import com.intellij.application.options.colors.ColorAndFontDescription
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.psi.TokenType
import com.extlang.engine.Terminal

/** Annotates the elements with their terminals' information
*/
public class ELAnnotator: Annotator {
    public override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val tok = element.getNode()!!.getElementType()
        when (tok)
        {
            is ELToken -> if (tok.Sym is Terminal) holder.createWeakWarningAnnotation(element, "Type: ${tok.Sym}")!!
            TokenType.BAD_CHARACTER -> holder.createErrorAnnotation(element, "Invalid character")!!
            else -> {
            }

        }
    }
}