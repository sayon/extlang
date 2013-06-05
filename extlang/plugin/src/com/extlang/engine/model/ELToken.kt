package com.extlang.engine

import com.intellij.psi.tree.IElementType
import java.util.HashMap
import com.extlang.engine.model.ExtendedSyntax
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.codeInsight.daemon.impl.quickfix.AddNewArrayExpressionFix
import com.sun.javaws.exceptions.InvalidArgumentException
import com.intellij.psi.tree.TokenSet


open class ELToken(public val Term: Symbol): IElementType(Term.Name, ELLanguage.INSTANCE)
{
    public val isNonTerminal: Boolean

    {
        if (Term is NonTerminal) isNonTerminal = true
        else isNonTerminal = false
    }

    class object {
        //todo do we really need this one? We can use 'is TokIdentifier'
        public fun AllIdentifiers() : TokenSet
            {
                return _allIdentifiers
            }

        var _allIdentifiers : TokenSet= TokenSet.EMPTY

        val _existingTerminalTokens: HashMap<Terminal, ELToken> = HashMap<Terminal, ELToken>() ;
        val _existingNonTerminalTokens: HashMap<NonTerminal, ELToken> = HashMap<NonTerminal, ELToken>() ;
        val _existingIdentifierTokens: HashMap<String, TokIdentifier> = HashMap<String, TokIdentifier>() ;

        //ctor
        {
            reinitializeTokens()
        }

        public fun reinitializeTokens() {
            _existingTerminalTokens.clear()
            _existingNonTerminalTokens.clear()
            _existingIdentifierTokens.clear()
            _allIdentifiers = TokenSet.EMPTY
            // for( kvp in ExtendedSyntax.Instance.Terminals)
            //     addTerm(kvp.getValue())
        }

        public fun fromTerminal(t: Terminal): ELToken
        {
            if (t == TermIdent.Instance)
                throw InvalidArgumentException(
                        array("fromTerminal should only be called on non-identifier terminals. Use fromIdentifier instead")
                )
            val res = _existingTerminalTokens.get(t)
            if (res == null)
            {
                val newentry = ELToken(t)
                _existingTerminalTokens.put(t, newentry)
                return newentry
            }
            else return res
        }

        public fun fromIdentifier(name: String): ELToken
        {
            val res = _existingIdentifierTokens.get(name)
            if (res == null)
            {
                val newentry = TokIdentifier(name)
                _existingIdentifierTokens.put(name, newentry)
                return newentry
            }
            return res
        }

        public fun fromNonTerminal(nt: NonTerminal): ELToken
        {
            val res = _existingNonTerminalTokens.get(nt)
            if (res == null)
            {
                val newentry = ELToken(nt)
                _existingNonTerminalTokens.put(nt, newentry)

                _allIdentifiers = TokenSet.orSet(_allIdentifiers, TokenSet.create(newentry))
                return newentry
            }
            else return res
        }

        private fun addTerm(t: Terminal) {
            _existingTerminalTokens.put(t, ELToken(t))

        }
    }

    public override fun toString(): String =
            "[${Term.Name}]"
}

public class TokIdentifier(public val Name: String): ELToken(TermIdent.Instance)
{
    public override fun toString(): String =
            "[${Term.Name}:$Name]"
}
public class EndOfStream: ELToken(TermEndOfFile.Instance)
{
    class object {
        public val Instance: EndOfStream = EndOfStream()
    }
}