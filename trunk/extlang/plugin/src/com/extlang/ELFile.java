package com.extlang;

import com.extlang.ELCodeFileType;
import com.extlang.engine.ELLanguage;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class ELFile extends PsiFileBase {
    public ELFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, ELLanguage.object.$instance.getINSTANCE());
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return ELCodeFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "elang File";
    }

    @Override
    public Icon getIcon(int flags) {
        return super.getIcon(flags);
    }
}