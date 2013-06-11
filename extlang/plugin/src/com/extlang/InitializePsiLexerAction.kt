package com.extlang

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiFile
import com.intellij.openapi.actionSystem.LangDataKeys

/**
 * Created with IntelliJ IDEA.
 * User: Sayon
 * Date: 10.06.13
 * Time: 16:52
 * To change this template use File | Settings | File Templates.
 */

/*
class InitializePsiLexerAction(): AnAction()
{
    public override fun actionPerformed(e: AnActionEvent?) {

        var file: PsiFile? = e?.getData(LangDataKeys.PSI_FILE)
        if (file == null)
        {
            System.err.println("No valid file is given to build Psi Lexer")
        }
        else
        {
            PsiLexer.setRoot(file!!)
        }
    }

}           */