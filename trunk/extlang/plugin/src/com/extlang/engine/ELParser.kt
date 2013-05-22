package com.extlang.engine

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiBuilder.Marker
import com.intellij.lang.PsiParser
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import java.util.Stack
import java.util.Set
import java.util.HashSet

class ELASTNode(node: ASTNode): ASTWrapperPsiElement(node)
{
    class object {
        val Nodes : HashSet<ELASTNode> = HashSet<ELASTNode>()
    }
     {
        Nodes.add(this)
     }
}
class StackRecord()
{
    class object {

        fun fromMarker(marker: Marker, tokenType: ELToken): StackRecord
        {
            var sr = StackRecord ()
            sr._marker = marker
            sr._isMarker = true
            sr._tokenType = tokenType
            return sr
        }
        fun fromSymbol(symbol: Symbol): StackRecord
        {
            var sr = StackRecord ()
            sr._isMarker = false
            sr._symbol = symbol
            return sr
        }
    }

    private var _marker: Marker? = null
    private var _isMarker: Boolean = false
    private var _symbol: Symbol? = null
    private var _tokenType: ELToken? = null

    public val tokenType: ELToken?
        get() {
            return _tokenType
        }

    public val symbol: Symbol?
        get() {
            return _symbol
        }
    public val marker: Marker?
        get () {
            return _marker
        }

    public val isMarker: Boolean
        get() {
            return _isMarker
        }

    public val isSymbol: Boolean
        get() {
            return !_isMarker
        }
    public fun toString(): String
    {
        if (isMarker) return "[Marker for ${_tokenType!!.Term.Name}]"
        else return _symbol!!.Name
    }
    public fun done() {
        _marker!!.done(tokenType)
    }
}


class ELParseMachine(public val ParseTable: GrammarTable)
{
    val _symStack = Stack<StackRecord>();


    private fun pushSymbol(s: Symbol)
    {
        _symStack.push(StackRecord.fromSymbol(s))
    }
    private fun  hasSymbol(): Boolean
    {
        return _symStack.size > 0 && _symStack.peek()!!.isSymbol
    }

    private fun  hasMarker(): Boolean
    {
        return _symStack.size > 0 && _symStack.peek()!!.isMarker
    }

    private fun topSymbol(): Symbol?
    {
        if(  hasSymbol()) return _symStack.peek()!!.symbol
        else return null
    }
    private fun topMarker(): Marker?
    {
        if(  hasMarker()) return _symStack.peek()!!.marker
        else return null
    }

    private fun pushRule(rule: Rule)
    {
        for(elem in rule.reverse().filter {(s) -> s != TermEpsilon.Instance })
            _symStack.push(StackRecord.fromSymbol(elem))
    }
    private fun pushMarker(marker: Marker, symbol: NonTerminal)
    {
        //System.err.println("Push marker for $symbol")
        val sr = StackRecord.fromMarker(marker, ELToken.fromNonTerminal(symbol)) ;
        _symStack.push(sr)
    }

    public fun parse (root: IElementType?, builder: PsiBuilder?): ASTNode
    {
        val currentToken = {(): IElementType? ->
            if (builder!!.getTokenType() == null)
                ELToken.fromTerminal(TermEndOfFile.Instance)
            else  builder.getTokenType()
        }

        pushSymbol(TermEndOfFile.Instance)
        pushSymbol(ParseTable.SyntaxProvided.Starter)
        val rootMarker = builder!!.mark()

        var error = false
        while(!error && (!builder.eof() || _symStack.size > 1 ))
        {
            System.err.println(stackString)
            while (hasMarker())
            {
                val top = _symStack.peek()!!.tokenType
                // System.err.println("pop marker for ${top}")
                _symStack.pop()!!.done()
            }
            val ctoken = currentToken()

            when (ctoken)
            {
                TokenType.BAD_CHARACTER -> {
                    builder.error("Bad character")
                    builder.advanceLexer()
                }
                is ELToken ->
                    when  (topSymbol())
                    {
                        ctoken.Term ->
                            {
                                builder.advanceLexer()
                                _symStack.pop()
                            }
                        else ->
                            {
                                error = !applyRule(builder, ctoken)
                            }
                    }
                else -> {
                    builder.error("Bad element")
                    error = true
                }
            }
        }
        //now only markers should have left along with nonterms producing epsilons
        while( _symStack.size > 0)
        {
            if (hasMarker() )
                _symStack.pop()!!.done()
            else
                _symStack.pop()
        }
        //consume the rest
        while (!builder.eof())
            builder.advanceLexer()
        rootMarker!!.done(root)

        System.err.println(ELASTNode.Nodes.fold("", {(acc, elem) -> acc + elem.toString()}))
        return builder.getTreeBuilt()!!
    }

    private fun applyRule(builder: PsiBuilder, ctoken: ELToken): Boolean
    {
        val key = Pair(topSymbol(), ctoken.Term)
        if (!ParseTable.Table.containsKey(key))
        {
            System.err.println("No rule for ${key}")
            return false
        }
        val rules = ParseTable.Table[key]!!
        when (rules.size ){
            1 -> {
                val top = topSymbol() as NonTerminal
                _symStack.pop()
                pushMarker(builder.mark(), top)
                pushRule(rules.head)
                return true
            }
            0 -> {
                builder.error("No rule for (${(topSymbol() as NonTerminal).Name}, ${ctoken.Term.Name})")
                builder.advanceLexer()
                return false
            }
            else -> {
                builder.error("Multiple rules for (${(topSymbol() as NonTerminal).Name}, ${ctoken.Term.Name})")
                return false
            }
        }


    }

    /*    fun createNode (node: ASTNode ) : ELASTNode
        {

        }     */

    public val stackString: String
        get() {
            val sb = StringBuilder ()
            for (elem in _symStack) {
                sb.append(" ")
                sb.append(elem)
            }
            return sb.toString()
        }


}
class ELParser: PsiParser
{
    public override fun parse(root: IElementType?, builder: PsiBuilder?): ASTNode {
        if (GrammarTable.Instance != null)
        {
            return ELParseMachine(GrammarTable.Instance).parse(root, builder)
        }
        else
        {
            val m = builder!!.mark()

            while(!builder.eof())
            {
                builder.advanceLexer()
            }
            m!!.done(root)
            return builder.getTreeBuilt()!!
        }
    }

}


/*  public fun  oldparse (root: IElementType?, builder: PsiBuilder?): ASTNode
  {
      val currentToken = {(): IElementType? ->
          if (builder!!.getTokenType() == null)
              ELToken.fromTerminal(TermEndOfFile.Instance)
          else  builder.getTokenType()
      }
      builder!!.setDebugMode(true)
      _symStack.clear()
      _symStack.push(StackRecord.fromSymbol(TermEndOfFile.Instance))
      _symStack.push(StackRecord.fromSymbol(ParseTable.SyntaxProvided.Starter!!))
      val rootMarker = builder.mark()

      var error = false
      var counter = 0
      while(!error && (!builder.eof() && _symStack.size > 0 ))
      {
          if (counter++ > 200) break
          //First thingies  do not touch input stream
          if (_symStack.peek()!!.isSymbol && _symStack.peek()!!.symbol == TermEpsilon.Instance)
          {
              _symStack.pop()
              continue
          }
          if (_symStack.peek()!!.isMarker) {
              _symStack.pop()!!.done()
              continue
          }
          if (currentToken() == TokenType.BAD_CHARACTER)
          {
              builder.error("Bad character")
              builder.advanceLexer()
              continue
          }
          if (currentToken() is ELToken
          && _symStack.peek()!!.isSymbol
          && ( currentToken() as ELToken).Term == _symStack.peek()!!.symbol!!)
          {
              builder.advanceLexer();
              _symStack.pop()
              continue
          }


          //apply a rule
          if (_symStack.peek()!!.isSymbol && _symStack.peek()!!.symbol is NonTerminal)
          {
              val nonterm = _symStack.peek()!!.symbol!!
              if (!(currentToken() is ELToken))
              {
                  System.err.println("Not ELToken ${currentToken}")
                  builder.advanceLexer()
                  continue
              }
              val term = (currentToken() as ELToken).Term
              val rules = ParseTable.Table[Pair(nonterm, term)]!!
              when (rules.size ){
                  1 -> {
                      val rule = rules.head
                      val matchedNonTerm = _symStack.peek()!!.symbol!! as NonTerminal
                      _symStack.pop()
                      val record = StackRecord.fromMarker(builder.mark(), ELToken.fromNonTerminal(matchedNonTerm))
                      _symStack.push(record)
                      for(elem in rule!!.reverse())
                          _symStack.push(StackRecord.fromSymbol(elem))
                  }
                  0 -> {
                      builder.error("No rule for (${nonterm.Name}, ${term.Name})")
                      builder.advanceLexer()
                      error = true
                  }
                  else -> {
                      builder.error("Multiple rules for (${nonterm.Name}, ${term.Name})")
                      error = true;
                  }
              }
              continue
          }

          if (!(currentToken() is ELToken) )
          {
              builder.error("Token is not ELToken: ${currentToken().toString()}")
              builder.advanceLexer()
              continue;
          }
      }
      //now only markers should have left along with nonterms producing epsilons
      while( _symStack.size > 1)
      {
          if (!_symStack.peek()!!.isMarker && _symStack.peek()!!.symbol != TermEndOfFile.Instance)
          {
              System.err.println("Something left in parsing stack, not an undone marker! ${_symStack.peek()!!.symbol}")
              _symStack.pop()
          }
          else
              if ( _symStack.peek()!!.isMarker )
                  _symStack.pop()!!.done()
      }

      while (!builder.eof())
          builder.advanceLexer()
      rootMarker!!.done(root)
      return builder.getTreeBuilt()!!
  }          */