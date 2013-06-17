package com.extlang.engine

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import com.intellij.lang.impl.PsiBuilderImpl
import com.extlang.engine.parser.ELParseMachine

class ELParser: PsiParser
{
    public override fun parse(root: IElementType?, builder: PsiBuilder?): ASTNode {
        return if (GrammarTable.Instance != null)
            ELParseMachine(GrammarTable.Instance!!).parse(root, builder as PsiBuilderImpl)
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
