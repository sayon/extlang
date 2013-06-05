package com.extlang.highlighter

import com.extlang.ELLexer
import com.intellij.lexer.Lexer
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.editor.SyntaxHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import java.awt.*
import java.io.Reader
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.extlang.engine.ELToken
import com.extlang.engine.TokIdentifier
import com.extlang.engine.TermNumber
import com.extlang.engine.TermIdent
import com.intellij.openapi.editor.markup.EffectType

public open class ELSyntaxHighlighter(): SyntaxHighlighterBase() {
    public override fun getHighlightingLexer(): Lexer {
        return ELLexer()
    }
    public override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {

        if (tokenType is ELToken)
        {
            return when  (tokenType.Term)
            {
                TermIdent.Instance -> IDENTIFIER_KEYS
                TermNumber.Instance -> NUMBER_KEYS
                else -> TERMINAL_KEYS
            }
        }
        return BAD_CHAR_KEYS
    }

    class object {
        public val TERMINAL: TextAttributesKey = createTextAttributesKey("EL_TERMINAL", TextAttributes(Color.BLACK, null, null, null, Font.BOLD))
        public val IDENTIFIER: TextAttributesKey = createTextAttributesKey("EL_IDENTIFIER", TextAttributes(Color.DARK_GRAY, null, null, null, Font.ITALIC))
        public val NUMBER: TextAttributesKey = createTextAttributesKey("EL_NUMBER", TextAttributes(Color.BLUE, null, null, null, Font.BOLD))
        val BAD_CHARACTER: TextAttributesKey = createTextAttributesKey("EL_BAD_CHARACTER", TextAttributes(Color.RED, null, null, null, Font.BOLD))
        private val BAD_CHAR_KEYS: Array<TextAttributesKey> = array<TextAttributesKey>(BAD_CHARACTER)
        private val TERMINAL_KEYS: Array<TextAttributesKey> = array<TextAttributesKey>(TERMINAL)
        private val IDENTIFIER_KEYS: Array<TextAttributesKey> = array<TextAttributesKey>(IDENTIFIER)
        private val NUMBER_KEYS: Array<TextAttributesKey> = array<TextAttributesKey>(NUMBER)
        private val EMPTY_KEYS: Array<TextAttributesKey> = array()   ;
    }

}
