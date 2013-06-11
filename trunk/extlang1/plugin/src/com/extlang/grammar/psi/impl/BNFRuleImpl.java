// This is a generated file. Not intended for manual editing.
package com.extlang.grammar.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.extlang.grammar.psi.BNFSimpleTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.extlang.grammar.psi.*;

public class BNFRuleImpl extends ASTWrapperPsiElement implements BNFRule {

  public BNFRuleImpl(ASTNode node) {
    super(node);
  }

  @Override
  @NotNull
  public BNFLefthandside getLefthandside() {
    return findNotNullChildByClass(BNFLefthandside.class);
  }

  @Override
  @Nullable
  public BNFQuotedtree getQuotedtree() {
    return findChildByClass(BNFQuotedtree.class);
  }

  @Override
  @NotNull
  public BNFRighthandside getRighthandside() {
    return findNotNullChildByClass(BNFRighthandside.class);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BNFVisitor) ((BNFVisitor)visitor).visitRule(this);
    else super.accept(visitor);
  }

}
