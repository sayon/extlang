package com.extlang.engine

import com.intellij.lexer.LexerBase
import com.intellij.psi.PsiElement
import com.intellij.lang.ASTNode
import com.extlang.util.Util
import com.extlang.engine.model.TokIdentifier
import com.intellij.psi.tree.IElementType
import com.extlang.engine.parser.ELASTNode
import com.extlang.engine.model.ELToken


/**
This is a custom lexer that walks PSI tree and gives you all the 'leaf' tokens, including ones
occupying zero space.
This lexer should be initialized via init method in class object first!
Currently this class is not used anywhere.

*/

open class PsiLexer(): LexerBase()
{
    var _buffer: CharSequence? = null
    var _startOffset: Int = 0
    var _endOffset: Int = 0
    var _tokenStart = 0
    var _tokenEnd = 0
    var _currentPosition = 0

    class object {
        public var RootElement: PsiElement? = null
        var _nodesCount = 0
        var _nodes: List<ASTNode>? = null
        public fun init  (elem: PsiElement)
        {
            RootElement = elem
            _nodes = Util.CollectLeaves(RootElement!!.getNode())
            _nodesCount = if (_nodes != null) _nodes!!.count() else 0
        }

        fun PsiElement.textRepr(): String
        {
            val tok = this.getNode()!!.getElementType()!!
            val text = this.getText()
            if (tok is TokIdentifier) return tok.Name
            else return if (text != null) text else ""
        }

    }
    public override fun start(buffer: CharSequence?, startOffset: Int, endOffset: Int, initialState: Int) {
        _buffer = buffer
        _startOffset = startOffset
        _endOffset = endOffset
        _tokenStart = startOffset
        _tokenEnd = if (getCurrentNode() != null) ( getCurrentNode()!!.getTextLength() - 1 ) else 0
        _nodes = Util.CollectLeaves(RootElement!!.getNode())
        _nodesCount = if (_nodes != null) _nodes!!.count() else 0
        _currentPosition = 0
    }

    public override fun getState(): Int {
        return 0
    }

    public override fun getTokenType(): IElementType? {
        return if (_currentPosition < _nodesCount) getCurrentNode()?.getElementType()
        else null
    }
    public override fun getTokenStart(): Int {
        return _tokenStart
    }
    public override fun getTokenEnd(): Int {
        return _tokenEnd
    }

    public override fun advance() {
        val curNode = getCurrentNode()
        if (curNode != null)
        {
            _tokenStart += curNode.getTextLength()
            _currentPosition++
            val newnode = getCurrentNode()
            if ( newnode != null)
                _tokenEnd = _tokenStart + newnode.getTextLength()
        }
        System.err.println("token: $_tokenStart : $_tokenEnd -> ${getTokenType()}")
    }

    public override fun getBufferSequence(): CharSequence? {
        return _buffer
    }
    public override fun getBufferEnd(): Int {
        return _endOffset
    }

    private fun getCurrentNode  (): ASTNode? {
        if (_currentPosition >= _nodesCount) return null
        return _nodes?.get(_currentPosition)
    }

}