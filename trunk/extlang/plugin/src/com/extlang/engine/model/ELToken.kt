package com.extlang.engine

import com.intellij.psi.tree.IElementType
import java.util.HashMap
import com.extlang.engine.model.ExtendedSyntax
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.extapi.psi.ASTWrapperPsiElement


open class ELToken(public val Term: Symbol): IElementType(Term.Name, ELLanguage.INSTANCE)
{
    public val isNonTerminal : Boolean

    {
        if (Term is NonTerminal) isNonTerminal = true
        else isNonTerminal = false
    }

    class object {
        private val _existingTerminalTokens: HashMap<Terminal, ELToken> = HashMap<Terminal, ELToken>() ;
        private val _existingNonTerminalTokens: HashMap<NonTerminal, ELToken> = HashMap<NonTerminal, ELToken>() ;

        //ctor
        {
            reinitializeTokens()
        }

        public fun reinitializeTokens() {
            _existingTerminalTokens.clear()
            for( kvp in ExtendedSyntax.Instance.Terminals)
                addTerm(kvp.getValue())
        }

        public fun fromTerminal(t: Terminal): ELToken
        {
            val res = _existingTerminalTokens.get(t)
            if (res == null)
            {
                val newentry = ELToken(t)
                _existingTerminalTokens.put(t, newentry)
                return newentry
            }
            else return res
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
        private fun addTerm(t: Terminal) {
            _existingTerminalTokens.put(t, ELToken(t))
        }
    }

    public override fun toString(): String =
            "[${Term.Name}]"
}

public class EndOfStream : ELToken(TermEndOfFile.Instance)
{
    class object {
        public val Instance : EndOfStream = EndOfStream()
    }
}