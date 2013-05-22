package com.extlang

import com.extlang.engine.GrammarBuilder
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.psi.PsiFile
import com.extlang.engine.FixedSyntax
import com.extlang.engine.ELParser
import com.extlang.engine.ELParseMachine
import com.extlang.engine.GrammarTable
import com.intellij.notification.EventLog
import com.intellij.openapi.vcs.changes.committed.VcsConfigurationChangeListener.Notification
import com.intellij.ui.popup.PopupComponent.Factory.Dialog
import com.intellij.openapi.ui.Messages
import com.extlang.grammar.BNFLanguage

public open class GrammarAction(): AnAction() {
    public override fun actionPerformed(e: AnActionEvent?): Unit {
        var file: PsiFile? = e?.getData(LangDataKeys.PSI_FILE)
        if (file == null || file!!.getLanguage() != BNFLanguage.INSTANCE!!)
        {
            Messages.showMessageDialog("No grammar file given!", "Error", Messages.getErrorIcon())
        }
        else
        {
            System.err.println("Building grammar from file ${file!!.getName()}")

            FixedSyntax.Instance = GrammarBuilder(file?.getNode()).SyntaxBuilt
            GrammarTable.Instance = GrammarTable(FixedSyntax.Instance)

            System.err.println(FixedSyntax.Instance.representation())
            System.err.println(GrammarTable.Instance!!.representation())
        }
    }


}
