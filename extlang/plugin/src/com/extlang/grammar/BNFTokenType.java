package com.extlang.grammar;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: sayon
 * Date: 11.05.13
 * Time: 18:41
 * To change this template use File | Settings | File Templates.
 */
public class BNFTokenType extends IElementType {
    public BNFTokenType(@NotNull @NonNls String debugName) {
        super(debugName, BNFLanguage.object$.getINSTANCE());
    }
}
