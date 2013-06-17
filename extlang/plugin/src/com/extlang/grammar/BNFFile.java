package com.extlang.grammar;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


public class BNFFile extends PsiFileBase {
    public BNFFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, BNFLanguage.object$.getINSTANCE());
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return BNFFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Grammar definition file for extensible language";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}