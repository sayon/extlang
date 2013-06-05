package com.extlang.grammar;

import com.extlang.grammar.BNFLanguage;
import com.intellij.lang.Language;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created with IntelliJ IDEA.
 * User: sayon
 * Date: 11.05.13
 * Time: 18:41
 * To change this template use File | Settings | File Templates.
 */
public class BNFTokenType extends IElementType {
    public BNFTokenType(@NotNull @NonNls String debugName) {
        super(debugName, BNFLanguage.object.$instance.getINSTANCE());
    }
}
