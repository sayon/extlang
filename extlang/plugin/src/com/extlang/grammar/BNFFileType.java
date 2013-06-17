package com.extlang.grammar;

import com.extlang.Icons;
import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class BNFFileType extends LanguageFileType {
    public static final BNFFileType INSTANCE = new BNFFileType();

    private BNFFileType() {
        super(BNFLanguage.object$.getINSTANCE());
    }

    @NotNull
    @Override
    public String getName() {
        return "grammar";
    }

    @NotNull
    @Override
    public String getDescription() {
        return "Extensible grammar description file";
    }

    @NotNull
    @Override
    public String getDefaultExtension() {
        return "grammar";
    }

    @Nullable
    @Override
    public Icon getIcon() {
        return Icons.GRAMMARFILE;
    }
}