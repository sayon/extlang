// This is a generated file. Not intended for manual editing.
package com.extlang.grammar.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.extlang.grammar.psi.*;

public class BNFTerminalsImpl extends ASTWrapperPsiElement implements BNFTerminals {

  public BNFTerminalsImpl(ASTNode node) {
    super(node);
  }

  @Override
  @NotNull
  public List<BNFTerminaldescription> getTerminaldescriptionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, BNFTerminaldescription.class);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BNFVisitor) ((BNFVisitor)visitor).visitTerminals(this);
    else super.accept(visitor);
  }

}
