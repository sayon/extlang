package com.extlang.formatter

import com.intellij.formatting.FormattingModelBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.formatting.FormattingModel
import com.intellij.psi.PsiFile
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.Wrap
import com.intellij.formatting.WrapType
import com.intellij.formatting.SpacingBuilder
import com.extlang.engine.model.ELToken
import com.intellij.formatting.Alignment
import com.extlang.engine.TermNumber

public open class ELFormattingModelBuilder(): FormattingModelBuilder {
    public override fun createModel(element: PsiElement?, settings: CodeStyleSettings?): FormattingModel {
        return FormattingModelProvider.createFormattingModelForPsiFile(
                element?.getContainingFile(),
                ELBlock(element?.getNode()!!,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        createSpaceBuilder(settings)!!),
                settings)!!
    }
    public override fun getRangeAffectingIndent(file: PsiFile?, offset: Int, elementAtOffset: ASTNode?): TextRange? {
        return null
    }

    private open fun createSpaceBuilder(settings: CodeStyleSettings?): SpacingBuilder? {
        var spacing = SpacingBuilder(settings)
        val punctuation = ELToken.PunctuationTokens()

        val terminalTokens = ELToken.TerminalTokens().filterNot {(tok) -> punctuation.containsItem(tok) && tok.Sym != TermNumber.Instance }
        spacing = terminalTokens.fold(spacing,
                {(sp, token) -> sp.around(token)!!.spaces(2)!! })

        spacing = punctuation.fold(spacing,
                {(sp, token) -> sp.before(token)!!.none()!!.after(token)!!.spaces(1)!! })

        return spacing
    }

}
