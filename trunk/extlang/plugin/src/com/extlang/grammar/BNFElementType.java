package com.extlang.grammar;

import com.extlang.grammar.BNFLanguage;
import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BNFElementType extends IElementType {
     public BNFElementType(@NotNull @NonNls String debugName) {
        super(debugName, BNFLanguage.object.$instance.getINSTANCE());
    }
}
