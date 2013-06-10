package com.extlang;

import com.extlang.grammar.BNFFileType;
import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class ELFileTypeFactory extends FileTypeFactory{
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        fileTypeConsumer.consume(ELCodeFileType.INSTANCE, "elang");
        fileTypeConsumer.consume(BNFFileType.INSTANCE, "grammar");
    }
}