// This is a generated file. Not intended for manual editing.
package com.extlang.grammar.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.extlang.grammar.psi.*;

public class BNFTerminaldescriptionImpl extends ASTWrapperPsiElement implements BNFTerminaldescription {

  public BNFTerminaldescriptionImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BNFVisitor) ((BNFVisitor)visitor).visitTerminaldescription(this);
    else super.accept(visitor);
  }

}
