// This is a generated file. Not intended for manual editing.
package com.extlang.grammar.psi;

import org.jetbrains.annotations.*;
import com.intellij.lang.LighterASTNode;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.openapi.diagnostic.Logger;
import static com.extlang.grammar.psi.BNFSimpleTypes.*;
import static com.extlang.grammar.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class BNFParser implements PsiParser {

  public static Logger LOG_ = Logger.getInstance("com.extlang.grammar.psi.BNFParser");

  @NotNull
  public ASTNode parse(IElementType root_, PsiBuilder builder_) {
    int level_ = 0;
    boolean result_;
    builder_ = adapt_builder_(root_, builder_, this);
    if (root_ == LEFTHANDSIDE) {
      result_ = lefthandside(builder_, level_ + 1);
    }
    else if (root_ == NODECONTENT) {
      result_ = nodecontent(builder_, level_ + 1);
    }
    else if (root_ == NONTERMINALS) {
      result_ = nonterminals(builder_, level_ + 1);
    }
    else if (root_ == NTTERMINALORALIASED) {
      result_ = ntterminaloraliased(builder_, level_ + 1);
    }
    else if (root_ == QUOTEDTREE) {
      result_ = quotedtree(builder_, level_ + 1);
    }
    else if (root_ == RIGHTHANDSIDE) {
      result_ = righthandside(builder_, level_ + 1);
    }
    else if (root_ == RULE) {
      result_ = rule(builder_, level_ + 1);
    }
    else if (root_ == RULES) {
      result_ = rules(builder_, level_ + 1);
    }
    else if (root_ == STARTER) {
      result_ = starter(builder_, level_ + 1);
    }
    else if (root_ == SYMBOL) {
      result_ = symbol(builder_, level_ + 1);
    }
    else if (root_ == TERMINALDESCRIPTION) {
      result_ = terminaldescription(builder_, level_ + 1);
    }
    else if (root_ == TERMINALS) {
      result_ = terminals(builder_, level_ + 1);
    }
    else {
      Marker marker_ = builder_.mark();
      result_ = parse_root_(root_, builder_, level_);
      while (builder_.getTokenType() != null) {
        builder_.advanceLexer();
      }
      marker_.done(root_);
    }
    return builder_.getTreeBuilt();
  }

  protected boolean parse_root_(final IElementType root_, final PsiBuilder builder_, final int level_) {
    return BNFFile(builder_, level_ + 1);
  }

  /* ********************************************************** */
  // nonterminals starter terminals rules
  static boolean BNFFile(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "BNFFile")) return false;
    if (!nextTokenIs(builder_, NONTERMINALSCAPTION)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = nonterminals(builder_, level_ + 1);
    result_ = result_ && starter(builder_, level_ + 1);
    result_ = result_ && terminals(builder_, level_ + 1);
    result_ = result_ && rules(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // ntterminaloraliased
  public static boolean lefthandside(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "lefthandside")) return false;
    if (!nextTokenIs(builder_, LEFTBRACE) && !nextTokenIs(builder_, NTIDENTIFIER)
        && replaceVariants(builder_, 2, "<lefthandside>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<lefthandside>");
    result_ = ntterminaloraliased(builder_, level_ + 1);
    if (result_) {
      marker_.done(LEFTHANDSIDE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // ALIAS | NTIDENTIFIER | TIDENTIFIER
  public static boolean nodecontent(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nodecontent")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<nodecontent>");
    result_ = consumeToken(builder_, ALIAS);
    if (!result_) result_ = consumeToken(builder_, NTIDENTIFIER);
    if (!result_) result_ = consumeToken(builder_, TIDENTIFIER);
    if (result_) {
      marker_.done(NODECONTENT);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // NONTERMINALSCAPTION CRLF (NTIDENTIFIER CRLF?)* CRLF*
  public static boolean nonterminals(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonterminals")) return false;
    if (!nextTokenIs(builder_, NONTERMINALSCAPTION)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, NONTERMINALSCAPTION, CRLF);
    result_ = result_ && nonterminals_2(builder_, level_ + 1);
    result_ = result_ && nonterminals_3(builder_, level_ + 1);
    if (result_) {
      marker_.done(NONTERMINALS);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // (NTIDENTIFIER CRLF?)*
  private static boolean nonterminals_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonterminals_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!nonterminals_2_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "nonterminals_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // NTIDENTIFIER CRLF?
  private static boolean nonterminals_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonterminals_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, NTIDENTIFIER);
    result_ = result_ && nonterminals_2_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // CRLF?
  private static boolean nonterminals_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonterminals_2_0_1")) return false;
    consumeToken(builder_, CRLF);
    return true;
  }

  // CRLF*
  private static boolean nonterminals_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "nonterminals_3")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!consumeToken(builder_, CRLF)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "nonterminals_3");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // NTIDENTIFIER |  LEFTBRACE NTIDENTIFIER AS ALIAS RIGHTBRACE
  public static boolean ntterminaloraliased(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ntterminaloraliased")) return false;
    if (!nextTokenIs(builder_, LEFTBRACE) && !nextTokenIs(builder_, NTIDENTIFIER)
        && replaceVariants(builder_, 2, "<ntterminaloraliased>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<ntterminaloraliased>");
    result_ = consumeToken(builder_, NTIDENTIFIER);
    if (!result_) result_ = ntterminaloraliased_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(NTTERMINALORALIASED);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // LEFTBRACE NTIDENTIFIER AS ALIAS RIGHTBRACE
  private static boolean ntterminaloraliased_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "ntterminaloraliased_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, LEFTBRACE, NTIDENTIFIER, AS, ALIAS, RIGHTBRACE);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // LEFTANGLE nodecontent    (SEMICOLON quotedtree* )? RIGHTANGLE
  public static boolean quotedtree(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quotedtree")) return false;
    if (!nextTokenIs(builder_, LEFTANGLE)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, LEFTANGLE);
    result_ = result_ && nodecontent(builder_, level_ + 1);
    result_ = result_ && quotedtree_2(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHTANGLE);
    if (result_) {
      marker_.done(QUOTEDTREE);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // (SEMICOLON quotedtree* )?
  private static boolean quotedtree_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quotedtree_2")) return false;
    quotedtree_2_0(builder_, level_ + 1);
    return true;
  }

  // SEMICOLON quotedtree*
  private static boolean quotedtree_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quotedtree_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, SEMICOLON);
    result_ = result_ && quotedtree_2_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // quotedtree*
  private static boolean quotedtree_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "quotedtree_2_0_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!quotedtree(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "quotedtree_2_0_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // symbol+
  public static boolean righthandside(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "righthandside")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<righthandside>");
    result_ = symbol(builder_, level_ + 1);
    int offset_ = builder_.getCurrentOffset();
    while (result_) {
      if (!symbol(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "righthandside");
        break;
      }
      offset_ = next_offset_;
    }
    if (result_) {
      marker_.done(RIGHTHANDSIDE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // lefthandside EQUAL righthandside   ARROW LEFTBRACE quotedtree RIGHTBRACE
  // |   lefthandside EQUAL righthandside
  public static boolean rule(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rule")) return false;
    if (!nextTokenIs(builder_, LEFTBRACE) && !nextTokenIs(builder_, NTIDENTIFIER)
        && replaceVariants(builder_, 2, "<rule>")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<rule>");
    result_ = rule_0(builder_, level_ + 1);
    if (!result_) result_ = rule_1(builder_, level_ + 1);
    if (result_) {
      marker_.done(RULE);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  // lefthandside EQUAL righthandside   ARROW LEFTBRACE quotedtree RIGHTBRACE
  private static boolean rule_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rule_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = lefthandside(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQUAL);
    result_ = result_ && righthandside(builder_, level_ + 1);
    result_ = result_ && consumeTokens(builder_, 0, ARROW, LEFTBRACE);
    result_ = result_ && quotedtree(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, RIGHTBRACE);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // lefthandside EQUAL righthandside
  private static boolean rule_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rule_1")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = lefthandside(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, EQUAL);
    result_ = result_ && righthandside(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // RULESCAPTION CRLF (rule SEMICOLON CRLF*)*
  public static boolean rules(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rules")) return false;
    if (!nextTokenIs(builder_, RULESCAPTION)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, RULESCAPTION, CRLF);
    result_ = result_ && rules_2(builder_, level_ + 1);
    if (result_) {
      marker_.done(RULES);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // (rule SEMICOLON CRLF*)*
  private static boolean rules_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rules_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!rules_2_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "rules_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // rule SEMICOLON CRLF*
  private static boolean rules_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rules_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = rule(builder_, level_ + 1);
    result_ = result_ && consumeToken(builder_, SEMICOLON);
    result_ = result_ && rules_2_0_2(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // CRLF*
  private static boolean rules_2_0_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "rules_2_0_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!consumeToken(builder_, CRLF)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "rules_2_0_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  /* ********************************************************** */
  // STARTERCAPTION CRLF NTIDENTIFIER CRLF+
  public static boolean starter(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "starter")) return false;
    if (!nextTokenIs(builder_, STARTERCAPTION)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, STARTERCAPTION, CRLF, NTIDENTIFIER);
    result_ = result_ && starter_3(builder_, level_ + 1);
    if (result_) {
      marker_.done(STARTER);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // CRLF+
  private static boolean starter_3(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "starter_3")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeToken(builder_, CRLF);
    int offset_ = builder_.getCurrentOffset();
    while (result_) {
      if (!consumeToken(builder_, CRLF)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "starter_3");
        break;
      }
      offset_ = next_offset_;
    }
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  /* ********************************************************** */
  // TIDENTIFIER | ntterminaloraliased
  public static boolean symbol(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "symbol")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    enterErrorRecordingSection(builder_, level_, _SECTION_GENERAL_, "<symbol>");
    result_ = consumeToken(builder_, TIDENTIFIER);
    if (!result_) result_ = ntterminaloraliased(builder_, level_ + 1);
    if (result_) {
      marker_.done(SYMBOL);
    }
    else {
      marker_.rollbackTo();
    }
    result_ = exitErrorRecordingSection(builder_, level_, result_, false, _SECTION_GENERAL_, null);
    return result_;
  }

  /* ********************************************************** */
  // TIDENTIFIER TREPR
  public static boolean terminaldescription(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "terminaldescription")) return false;
    if (!nextTokenIs(builder_, TIDENTIFIER)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, TIDENTIFIER, TREPR);
    if (result_) {
      marker_.done(TERMINALDESCRIPTION);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  /* ********************************************************** */
  // TERMINALSCAPTION CRLF (terminaldescription CRLF*)*
  public static boolean terminals(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "terminals")) return false;
    if (!nextTokenIs(builder_, TERMINALSCAPTION)) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = consumeTokens(builder_, 0, TERMINALSCAPTION, CRLF);
    result_ = result_ && terminals_2(builder_, level_ + 1);
    if (result_) {
      marker_.done(TERMINALS);
    }
    else {
      marker_.rollbackTo();
    }
    return result_;
  }

  // (terminaldescription CRLF*)*
  private static boolean terminals_2(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "terminals_2")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!terminals_2_0(builder_, level_ + 1)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "terminals_2");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

  // terminaldescription CRLF*
  private static boolean terminals_2_0(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "terminals_2_0")) return false;
    boolean result_ = false;
    Marker marker_ = builder_.mark();
    result_ = terminaldescription(builder_, level_ + 1);
    result_ = result_ && terminals_2_0_1(builder_, level_ + 1);
    if (!result_) {
      marker_.rollbackTo();
    }
    else {
      marker_.drop();
    }
    return result_;
  }

  // CRLF*
  private static boolean terminals_2_0_1(PsiBuilder builder_, int level_) {
    if (!recursion_guard_(builder_, level_, "terminals_2_0_1")) return false;
    int offset_ = builder_.getCurrentOffset();
    while (true) {
      if (!consumeToken(builder_, CRLF)) break;
      int next_offset_ = builder_.getCurrentOffset();
      if (offset_ == next_offset_) {
        empty_element_parsed_guard_(builder_, offset_, "terminals_2_0_1");
        break;
      }
      offset_ = next_offset_;
    }
    return true;
  }

}
