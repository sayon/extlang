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

public class BNFRighthandsideImpl extends ASTWrapperPsiElement implements BNFRighthandside {

  public BNFRighthandsideImpl(ASTNode node) {
    super(node);
  }

  @Override
  @NotNull
  public List<BNFSymbol> getSymbolList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, BNFSymbol.class);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof BNFVisitor) ((BNFVisitor)visitor).visitRighthandside(this);
    else super.accept(visitor);
  }

}
