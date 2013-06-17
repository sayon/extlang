package com.extlang.grammar;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

public class BNFElementType extends IElementType {
     public BNFElementType(@NotNull @NonNls String debugName) {
         super(debugName, BNFLanguage.object$.getINSTANCE());
     }
}
