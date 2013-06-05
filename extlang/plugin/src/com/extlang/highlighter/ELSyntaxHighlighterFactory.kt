package com.extlang.highlighter

import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class ELSyntaxHightlighterFactory(): SyntaxHighlighterFactory () {
    public override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return  ELSyntaxHighlighter();
    }
}

