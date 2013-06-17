package com.extlang;

import com.extlang.grammar.psi.BNFSimpleTypes
import com.extlang.grammar.BNFTokenType
import com.extlang.util.*
import com.intellij.lexer.LexerBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import java.util.HashMap
import com.intellij.openapi.diagnostic.*
import com.extlang.parser.BNFParserDefinition

/* We use token types derived from Token as well as standard types defined in TokenType.java*/
public open class BNFLexer(): LexerBase() {
    public override fun getBufferEnd(): Int {
        return _endOffset
    }
    public override fun getTokenStart(): Int {
        return _tokenStartOffset
    }
    public override fun getTokenEnd(): Int {
        return _tokenEndOffset
    }
    public override fun getBufferSequence(): CharSequence? {
        throw UnsupportedOperationException()
    }
    val ALIAS_STARTING_SYMBOL = '$'
    fun isAllowedIdentifierCharacter (c: Char): Boolean
    {
        return c.isJavaIdentifierStart() || c == ALIAS_STARTING_SYMBOL || c == '\''
    }
    class object {
        public val _keywordToToken: HashMap<String, IElementType> = HashMap<String, IElementType>();
        {
            _keywordToToken.put("as", BNFSimpleTypes.AS);
            _keywordToToken.put(";", BNFSimpleTypes.SEMICOLON);
            _keywordToToken.put("->", BNFSimpleTypes.ARROW);
            _keywordToToken.put("::=", BNFSimpleTypes.EQUAL);
            _keywordToToken.put("<", BNFSimpleTypes.LEFTANGLE);
            _keywordToToken.put(">", BNFSimpleTypes.RIGHTANGLE);
            _keywordToToken.put("{", BNFSimpleTypes.LEFTBRACE);
            _keywordToToken.put("}", BNFSimpleTypes.RIGHTBRACE);
            _keywordToToken.put("--nonterminals:", BNFSimpleTypes.NONTERMINALSCAPTION);
            _keywordToToken.put("--starter:", BNFSimpleTypes.STARTERCAPTION);
            _keywordToToken.put("--terminals:", BNFSimpleTypes.TERMINALSCAPTION);
            _keywordToToken.put("--rules:", BNFSimpleTypes.RULESCAPTION);
        }
    }
    private var _buffer: CharSequence? = null
    private var _startOffset: Int = 0
    private var _endOffset: Int = 0
    private var _initialState: Int = 0
    private var _currentState: Int = 0
    private var _currentLexerPosition: Int = 0

    private var _currentToken: IElementType? = null

    var _tokenStartOffset = 0
    var _tokenEndOffset = 0


    public override fun start(buffer: CharSequence?, startOffset: Int, endOffset: Int, initialState: Int) {
        _buffer = buffer
        _startOffset = startOffset
        _endOffset = endOffset
        _initialState = initialState
        _currentState = initialState
        _currentLexerPosition = _startOffset
        advance()
    }

    public override fun getState(): Int {
        return 0 // for now, no scopes or anything
    }

    public override fun getTokenType(): IElementType? {

        // System.err.println("getTokenType: ${(if (_currentToken == null) "null" else _currentToken.toString())}")
        return _currentToken
    }


    public override fun advance() {
        val startedAtPosition = _currentLexerPosition
        _currentToken = selectNext()
        _tokenStartOffset = startedAtPosition
        _tokenEndOffset = _currentLexerPosition
    }

    private fun selectNext(): IElementType?
    {
        if (_currentLexerPosition >= _endOffset)
            return null

        if (tryConsumeCRLF())
            return  BNFSimpleTypes.CRLF

        if (tryConsumeWhitespace())
            return   TokenType.WHITE_SPACE

        val term = tryGetKeyword()
        if (term != null)
            return term

        if (tryGetQuoted())
        {
            return BNFSimpleTypes.TREPR
        }
        if (tryGetComment())
        {
            return BNFParserDefinition.COMMENT
        }

        val symbol = tryGetWord()

        if (symbol.length == 0)
        {
            _currentLexerPosition++
            return TokenType.BAD_CHARACTER
        }

        if (symbol[0] == ALIAS_STARTING_SYMBOL)
            return BNFSimpleTypes.ALIAS

        if (symbol.all { c-> c.isUpperCase() }  )
            return BNFSimpleTypes.TIDENTIFIER
        return BNFSimpleTypes.NTIDENTIFIER
    }

    private fun tryGetNumber(): Int?
    {
        var currentPeekPosition = _currentLexerPosition
        if (currentPeekPosition >= _endOffset) return null
        val isSigned = _buffer!![_currentLexerPosition] == '-'

        if (isSigned) currentPeekPosition++
        while( currentPeekPosition < _endOffset && _buffer!![currentPeekPosition].isDigit())
            currentPeekPosition++

        val limit = if (isSigned) 1 else 0
        if (currentPeekPosition - _currentLexerPosition > limit )
        {
            val retval = Integer.parseInt(_buffer!!.subSequence(_currentLexerPosition, currentPeekPosition).toString())
            _currentLexerPosition = currentPeekPosition
            return retval
        }
        else return null
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
    private fun tryConsumeCRLF(): Boolean
    {
        if (_buffer!!.compareWith(_currentLexerPosition, "\n"))
        {
            _currentLexerPosition++
            return true
        }
        else
            return false
    }

    private fun tryGetKeyword(): BNFTokenType?
    {
        val symbolsLeft = _endOffset - _currentLexerPosition
        for( key in _keywordToToken.keySet() )
        {
            if (key.length > symbolsLeft) continue
            if (_buffer!!.compareWith(_currentLexerPosition, key))
            {
                _currentLexerPosition += key.length
                return _keywordToToken[key] as BNFTokenType
            }
        }
        return null
    }

    private fun tryGetWord(): String
    {
        var currentPeekPosition = _currentLexerPosition
        while (currentPeekPosition < _endOffset
        && isAllowedIdentifierCharacter(_buffer!![currentPeekPosition]) )
        {
            currentPeekPosition++
        }

        val retval = _buffer!!.subSequence(_currentLexerPosition, currentPeekPosition).toString()
        _currentLexerPosition = currentPeekPosition
        return retval
    }
    private fun tryGetQuoted(): Boolean
    {
        var currentPeekPosition = _currentLexerPosition
        if (_buffer!![currentPeekPosition] == '"')
        {
            currentPeekPosition++

            while (currentPeekPosition < _endOffset
            && _buffer!![currentPeekPosition] != '"')
            {
                currentPeekPosition++
            }
            if (currentPeekPosition < _endOffset && _buffer!![currentPeekPosition] == '"')
            {
                currentPeekPosition++
                //val retval = _buffer!!.subSequence(_currentLexerPosition, currentPeekPosition).toString()
                _currentLexerPosition = currentPeekPosition
                return  true
            }
        }

        return false
    }
    private fun tryGetComment(): Boolean
    {
        var currentPeekPosition = _currentLexerPosition
        if (_buffer!![currentPeekPosition] == '#')
        {
            currentPeekPosition++

            while (currentPeekPosition < _endOffset
            && _buffer!![currentPeekPosition] != '"')
            {
                currentPeekPosition++
            }
            if (currentPeekPosition < _endOffset &&
            ( _buffer!![currentPeekPosition] == '#'
            || _buffer!![currentPeekPosition] == '\n' ) )
            {
                currentPeekPosition++
                _currentLexerPosition = currentPeekPosition
                return  true
            }
        }

        return false
    }

}

