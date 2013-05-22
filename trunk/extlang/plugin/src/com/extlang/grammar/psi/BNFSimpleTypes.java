// This is a generated file. Not intended for manual editing.
package com.extlang.grammar.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.extlang.grammar.BNFElementType;
import com.extlang.grammar.BNFTokenType;
import com.extlang.grammar.psi.impl.*;

public interface BNFSimpleTypes {

  IElementType LEFTHANDSIDE = new BNFElementType("LEFTHANDSIDE");
  IElementType NODECONTENT = new BNFElementType("NODECONTENT");
  IElementType NONTERMINALS = new BNFElementType("NONTERMINALS");
  IElementType NTTERMINALORALIASED = new BNFElementType("NTTERMINALORALIASED");
  IElementType QUOTEDTREE = new BNFElementType("QUOTEDTREE");
  IElementType RIGHTHANDSIDE = new BNFElementType("RIGHTHANDSIDE");
  IElementType RULE = new BNFElementType("RULE");
  IElementType RULES = new BNFElementType("RULES");
  IElementType STARTER = new BNFElementType("STARTER");
  IElementType SYMBOL = new BNFElementType("SYMBOL");
  IElementType TERMINALDESCRIPTION = new BNFElementType("TERMINALDESCRIPTION");
  IElementType TERMINALS = new BNFElementType("TERMINALS");

  IElementType ALIAS = new BNFTokenType("ALIAS");
  IElementType ARROW = new BNFTokenType("ARROW");
  IElementType AS = new BNFTokenType("AS");
  IElementType CRLF = new BNFTokenType("CRLF");
  IElementType EQUAL = new BNFTokenType("EQUAL");
  IElementType LEFTANGLE = new BNFTokenType("LEFTANGLE");
  IElementType LEFTBRACE = new BNFTokenType("LEFTBRACE");
  IElementType NONTERMINALSCAPTION = new BNFTokenType("NONTERMINALSCAPTION");
  IElementType NTIDENTIFIER = new BNFTokenType("NTIDENTIFIER");
  IElementType RIGHTANGLE = new BNFTokenType("RIGHTANGLE");
  IElementType RIGHTBRACE = new BNFTokenType("RIGHTBRACE");
  IElementType RULESCAPTION = new BNFTokenType("RULESCAPTION");
  IElementType SEMICOLON = new BNFTokenType("SEMICOLON");
  IElementType STARTERCAPTION = new BNFTokenType("STARTERCAPTION");
  IElementType TERMINALSCAPTION = new BNFTokenType("TERMINALSCAPTION");
  IElementType TIDENTIFIER = new BNFTokenType("TIDENTIFIER");
  IElementType TREPR = new BNFTokenType("TREPR");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == LEFTHANDSIDE) {
        return new BNFLefthandsideImpl(node);
      }
      else if (type == NODECONTENT) {
        return new BNFNodecontentImpl(node);
      }
      else if (type == NONTERMINALS) {
        return new BNFNonterminalsImpl(node);
      }
      else if (type == NTTERMINALORALIASED) {
        return new BNFNtterminaloraliasedImpl(node);
      }
      else if (type == QUOTEDTREE) {
        return new BNFQuotedtreeImpl(node);
      }
      else if (type == RIGHTHANDSIDE) {
        return new BNFRighthandsideImpl(node);
      }
      else if (type == RULE) {
        return new BNFRuleImpl(node);
      }
      else if (type == RULES) {
        return new BNFRulesImpl(node);
      }
      else if (type == STARTER) {
        return new BNFStarterImpl(node);
      }
      else if (type == SYMBOL) {
        return new BNFSymbolImpl(node);
      }
      else if (type == TERMINALDESCRIPTION) {
        return new BNFTerminaldescriptionImpl(node);
      }
      else if (type == TERMINALS) {
        return new BNFTerminalsImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
