package com.extlang.engine

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.psi.tree.IElementType

import com.intellij.lang.PsiBuilder.Marker
import com.intellij.lang.PsiParser
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import java.util.Stack
import java.util.HashSet
import com.intellij.psi.text.BlockSupport
import com.intellij.lang.impl.PsiBuilderImpl
import java.util.ArrayList
import com.extlang.engine.parser.ELParseMachine





class ELParser: PsiParser
{
    public override fun parse(root: IElementType?, builder: PsiBuilder?): ASTNode {
        return if (GrammarTable.Instance != null)
            ELParseMachine(GrammarTable.Instance).parse(root, builder as PsiBuilderImpl)
        else primitiveParse(root, builder)
    }

    private fun primitiveParse(root: IElementType?, builder: PsiBuilder?): ASTNode {

        val m = builder!!.mark()

        while(!builder.eof())
            builder.advanceLexer()
        m!!.done(root)
        return builder.getTreeBuilt()!!

    }


}
