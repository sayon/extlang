package com.extlang.engine.model

import com.intellij.psi.tree.IElementType
import java.util.HashMap
import com.extlang.engine.model.ExtendedSyntax
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.codeInsight.daemon.impl.quickfix.AddNewArrayExpressionFix
import com.sun.javaws.exceptions.InvalidArgumentException
import com.intellij.psi.tree.TokenSet
import com.extlang.engine.Symbol
import com.extlang.engine.ELLanguage
import com.extlang.engine.NonTerminal
import com.extlang.engine.Terminal
import com.extlang.engine.TermIdent
import com.extlang.engine.TermEndOfFile
import java.util.ArrayList

/**Extensible language token type
Always contains a non-terminal or a terminal inside.
There exist instance caches (as maps) for
- Identifiers (each identifier has his own TokIdentifier:ELToken instance
- Terminal tokens
- Nonterminal tokens

Identifiers cache exists also as a TokenSet instance and contains all present and past identifiers

Ctor clears caches

*/
public open class ELToken(public val Sym: Symbol): IElementType(Sym.Name, ELLanguage.INSTANCE)
{
    public val isNonTerminal: Boolean

    {
        if (Sym is NonTerminal) isNonTerminal = true
        else isNonTerminal = false
    }

    class object {
        public fun AllIdentifiers(): TokenSet
        {
            return _allIdentifiers
        }

        // Only identifiers cache. Can grow big, and is a potential bottleneck.
        //todo : Maybe we should clear it from time to time
        private var _allIdentifiers: TokenSet = TokenSet.EMPTY

        //terminals cache
        private val _existingTerminalTokens: HashMap<Terminal, ELToken> = HashMap<Terminal, ELToken>() ;
        //nonterminals cache
        private val _existingNonTerminalTokens: HashMap<NonTerminal, ELToken> = HashMap<NonTerminal, ELToken>() ;
        //identifier tokens cache. F.e. if there exists identifier named 'x', it will have a TokIdentifier instance
        //in this cache and lexer will use it whenever it sees this identifier.
        private val _existingIdentifierTokens: HashMap<String, TokIdentifier> = HashMap<String, TokIdentifier>() ;

        private var _punctuationOnlyTokens: ArrayList<ELToken> = ArrayList<ELToken>()
        public fun PunctuationTokens(): List<ELToken>
        {
            return _punctuationOnlyTokens
        }
        public fun TerminalTokens(): Collection<ELToken>
        {
            return _existingTerminalTokens.values()
        }

        public fun IdentifierTokens(): Collection<TokIdentifier>
        {
            return _existingIdentifierTokens.values()
        }
        //ctor
        {
            reinitializeTokens()
        }

        public fun reinitializeTokens() {

            _existingTerminalTokens.clear()
            _existingNonTerminalTokens.clear()
            _existingIdentifierTokens.clear()
            _allIdentifiers = TokenSet.EMPTY
            _punctuationOnlyTokens.clear()

        }

        //architecture-wise it looks like crap. I have even added TerminalsToReprs into FixedSyntax just for this
        private fun tryAddPunctuationToken(token: ELToken)
        {
            fun String.isPunctuationOnly(): Boolean =
                    foldRight(true, {(c, acc) -> acc && !(Character.isLetter(c) || Character.isDigit(c)) })

            if (ExtendedSyntax.Instance.TerminalsToReprs.containsKey(token.Sym) && !_punctuationOnlyTokens.contains(token))
            {
                val repr = ExtendedSyntax.Instance.TerminalsToReprs[token.Sym]!!
                if (repr.isPunctuationOnly())
                    _punctuationOnlyTokens.add(token)
            }
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
                tryAddPunctuationToken(newentry)
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
                _allIdentifiers = TokenSet.orSet(_allIdentifiers, TokenSet.create(newentry))
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
                return newentry
            }
            else return res
        }

        private fun addTermToken(t: Terminal) {
            _existingTerminalTokens.put(t, ELToken(t))
        }
    }

    public override fun toString(): String =
            "[${Sym.Name}]"
}

public class TokIdentifier(public val Name: String): ELToken(TermIdent.Instance)
{
    public override fun toString(): String =
            "[${Sym.Name}:$Name]"
}
public class EndOfStream: ELToken(TermEndOfFile.Instance)
{
    class object {
        public val Instance: EndOfStream = EndOfStream()
    }
}