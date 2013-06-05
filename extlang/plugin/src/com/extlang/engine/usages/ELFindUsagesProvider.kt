package com.extlang.engine.usages

import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.psi.PsiElement
import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.psi.tree.TokenSet
import com.extlang.ELLexer
import com.extlang.engine.ELToken
import com.extlang.engine.model.ExtendedSyntax
import com.extlang.parser.ELParserDefinition
import com.extlang.engine.TermNumber
import com.extlang.engine.TermIdent
import com.extlang.engine.parser.ELASTNode


public class ELFindUsagesProvider: FindUsagesProvider{

    class object
    {
        val scanner: DefaultWordsScanner;
        {
            val lexer = ELLexer()
            val identifiers = TokenSet.create(ELToken.fromTerminal(TermIdent.Instance))
            val comments = TokenSet.EMPTY
            val literals = TokenSet.create(ELToken.fromTerminal(TermNumber.Instance))
            scanner = DefaultWordsScanner(lexer, identifiers, comments, literals)
        }
    }
    public override fun getWordsScanner(): WordsScanner? {
        return scanner
    }
    public override fun canFindUsagesFor(psiElement: PsiElement): Boolean {

        val token = psiElement.getNode()!!.getElementType()
        return  token == ELToken.fromTerminal(TermIdent.Instance)

    }
    public override fun getHelpId(psiElement: PsiElement): String? =
            null

    public override fun getType(element: PsiElement): String {
        val token = element.getNode()?.getElementType()
        if (token is ELToken )
            return token.Term.Name
        return "UnknownType"
    }
    public override fun getDescriptiveName(element: PsiElement): String =
            "Description"  //descriptions can be supplied through grammar as well, can't they?

    public override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        val token = element.getNode()?.getElementType()
        if (token is ELToken )
            return "${element.getText()}<::${token.Term.Name}"
        return element.getText()!!
    }
}