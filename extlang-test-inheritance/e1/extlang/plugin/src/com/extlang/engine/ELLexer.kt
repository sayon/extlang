package com.extlang

import com.extlang.engine.ELToken
import com.extlang.engine.FixedSyntax
import com.extlang.engine.TermIdent
import com.extlang.engine.TermNumber
import com.extlang.engine.Terminal
import com.extlang.util.containsElement
import com.intellij.lexer.LexerBase
import com.intellij.lexer.LexerPosition
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.extlang.engine.model.ExtendedSyntax

/* We use token types derived from ELToken as well as standard types defined in TokenType.java*/
open class ELLexer(): LexerBase() {

    var _buffer: CharSequence? = null
    var _startOffset: Int = 0
    var _endOffset: Int = 0
    var _initialState: Int = 0
    var _currentState: Int = 0

    var _currentLexerPosition = 0


    public override fun start(buffer: CharSequence?, startOffset: Int, endOffset: Int, initialState: Int) {
        _buffer = buffer
        _startOffset = startOffset
        _endOffset = endOffset
        _initialState = initialState
        _currentState = initialState
        _currentLexerPosition = _startOffset


        position = getCurrentPosition()
        advance()
    }
    var position: LexerPosition? = null

    public override fun getState(): Int {
        return 0 // for now, no scopes or anything
    }

    var _tokenStartOffset = 0
    var _tokenEndOffset = 0


    public override fun getTokenType(): IElementType? {
        return _currentToken
    }

    private var _currentToken: IElementType? = null

    public override fun advance() {
        val startedAtPosition = _currentLexerPosition
        val next = selectNext();

        _currentToken = next
        _tokenStartOffset = startedAtPosition
        _tokenEndOffset = _currentLexerPosition
    }
    private fun selectNext(): IElementType?
    {
        if (_currentLexerPosition >= _endOffset)
            return  null

        if (tryConsumeWhitespace())
            return TokenType.WHITE_SPACE

        if (tryConsumeNumber())
            return ELToken.fromTerminal(TermNumber.Instance)

        // one of terminal symbols
        val term = tryConsumeTerminalToken(ExtendedSyntax.Instance.Reprs)
        if (term != null)
            return ELToken.fromTerminal(term)

        // identifier
        if (tryGetWord())  return ELToken.fromTerminal(TermIdent.Instance)

        _currentLexerPosition++
        return TokenType.BAD_CHARACTER
    }

    private fun tryConsumeNumber(): Boolean
    {
        var currentPeekPosition = _currentLexerPosition
        val hasSign = "+-".containsElement(_buffer!![_currentLexerPosition])
        if (hasSign) currentPeekPosition++

        while( currentPeekPosition < _endOffset && _buffer!![currentPeekPosition].isDigit())
            currentPeekPosition++

        val limit = if (hasSign) 1 else 0
        if (currentPeekPosition - _currentLexerPosition > limit )
        {
            _currentLexerPosition = currentPeekPosition
            return true
        }
        else return false
    }

    private fun tryConsumeWhitespace(): Boolean
    {
        var hasConsumedWhitespaces = false;
        if (_buffer != null)
            while (_currentLexerPosition < _endOffset && _buffer!![_currentLexerPosition].isWhitespace()) {
                _currentLexerPosition++
                hasConsumedWhitespaces = true;
            }
        return hasConsumedWhitespaces
    }

    private fun tryConsumeTerminalToken(terms: Map<String, Terminal>): Terminal?
    {
        val symbolsLeft = _endOffset - _currentLexerPosition
        for( key in terms.keySet() )
        {
            if (key.length > symbolsLeft) continue
            if ( peekWord(key))
            {
                _currentLexerPosition += key.length
                return terms[key]
            }
        }
        return null
    }
    private fun isAllowedIdentifierCharacter(c: Char): Boolean
    {
        return c.isJavaLetter()
    }

    private fun tryGetWord(): Boolean
    {
        var currentPeekPosition = _currentLexerPosition
        while (currentPeekPosition < _endOffset &&
        isAllowedIdentifierCharacter(_buffer!![currentPeekPosition])
        )
            currentPeekPosition++

        if (currentPeekPosition == _currentLexerPosition)
            return false

        _currentLexerPosition = currentPeekPosition
        return true
    }
    private fun peekWord(seq: String): Boolean {
        var pos = _currentLexerPosition
        for (element in seq)
        {
            if (element != _buffer!!.charAt(pos))
                return false
            pos++
        }
        return true
    }
    public override fun getTokenStart(): Int {
        return _tokenStartOffset;
    }
    public override fun getTokenEnd(): Int {
        return _tokenEndOffset;
    }
    public override fun getBufferSequence(): CharSequence? {
        return _buffer
    }
    public override fun getBufferEnd(): Int {
        return _endOffset
    }
}
