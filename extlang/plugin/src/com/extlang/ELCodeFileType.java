package com.extlang;

import com.extlang.engine.ELLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class ELCodeFileType extends LanguageFileType {
    public static final ELCodeFileType INSTANCE = new ELCodeFileType();

    private ELCodeFileType() {
        super(ELLanguage.object$.getINSTANCE());
    }

    @NotNull
    @Override
    public String getName() {
        return "elang";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Extensible Language file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "elang";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.CODEFILE;
    }
}