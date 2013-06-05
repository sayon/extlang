package com.extlang.util

import com.intellij.util.indexing.FileBasedIndex
import com.intellij.psi.search.FileTypeIndex
import com.extlang.ELCodeFileType
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.openapi.project.Project
import com.extlang.engine.parser.ELASTNode
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import java.util.ArrayList
import com.extlang.engine.TokIdentifier


class Util
{
    class object {
        public fun  findIdentifiers(project: Project, key: String? = null): List<ELASTNode>
        {
            val result = ArrayList<ELASTNode>()
            val virtualFiles = FileBasedIndex.getInstance()!!
                    .getContainingFiles(
                    FileTypeIndex.NAME,
                    ELCodeFileType.INSTANCE,
                    GlobalSearchScope.allScope(project))
            for (virtualFile in virtualFiles) {
                val file = PsiManager.getInstance(project).findFile(virtualFile)
                if (file != null)
                {
                    val identifiers = PsiTreeUtil.getChildrenOfType(file, javaClass<ELASTNode>())
                    if (identifiers != null)
                    {
                        for (identifier in identifiers)
                        {
                            System.err.println("Comparing key $key with identifier's name ${identifier.getName()}")
                            val tok = identifier.getNode().getElementType()
                            if (tok is TokIdentifier && (key == null || tok.Name == key))
                                result.add(identifier)

                        }
                    }
                }
            }
            return result
        }


    }
}
