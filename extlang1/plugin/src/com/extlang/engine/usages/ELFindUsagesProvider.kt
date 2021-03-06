package com.extlang.engine.usages

import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.psi.PsiElement
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.psi.tree.TokenSet
import com.extlang.ELLexer
import com.extlang.engine.model.ELToken
import com.extlang.engine.model.ExtendedSyntax
import com.extlang.parser.ELParserDefinition
import com.extlang.engine.TermNumber
import com.extlang.engine.TermIdent
import com.extlang.engine.parser.ELASTNode
import com.extlang.engine.model.TokIdentifier
import com.extlang.engine.model.ELToken
import com.intellij.lang.cacheBuilder.WordOccurrence
import com.intellij.util.Processor

import com.intellij.psi.tree.IElementType
import com.intellij.lexer.Lexer
import com.extlang.PsiLexer
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager

public class ELFindUsagesProvider: FindUsagesProvider{


    public override fun getWordsScanner(): WordsScanner? {
        return DefaultWordsScanner(ELLexer(), ELToken.AllIdentifiers(), TokenSet.EMPTY, TokenSet.EMPTY)
    }
    public override fun canFindUsagesFor(psiElement: PsiElement): Boolean {

        val child = psiElement.getNode()!!.getFirstChildNode()
        return  if (child == null)  false
        else child.getElementType() is TokIdentifier
    }
    public override fun getHelpId(psiElement: PsiElement): String? =
            null

    public override fun getType(element: PsiElement): String {
        val token = element.getNode()?.getElementType()
        if (token is ELToken )
            return token.Sym.Name
        return "UnknownType"
    }
    public override fun getDescriptiveName(element: PsiElement): String {
        return   "Element with name ${getNodeText(element, true)}"  //descriptions can be supplied through grammar as well, can't they?
    }
    public override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        val child = element.getFirstChild()
        if (child != null)
        {
            val token = child.getNode()!!.getElementType()
            if (token is TokIdentifier )
                return token.Name
        }
        return element.getText()!!
    }
}