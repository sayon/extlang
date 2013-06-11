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
import com.extlang.engine.model.TokIdentifier
import com.intellij.lang.ASTNode
import com.extlang.engine.model.ELToken


class Util
{

    class object {
        public fun CollectDescendants(root: ASTNode, pred: (ASTNode)-> Boolean): List<ASTNode>
        {
            fun walkTree(n: ASTNode, condition: (ASTNode)-> Boolean, accumulator: ArrayList<ASTNode>): ArrayList<ASTNode>
            {
                if ( condition.invoke(n) ) {
                    accumulator.add(n)
                }
                for (c in n.getChildren(null)!!)
                {
                    walkTree(c, condition, accumulator)
                }
                return accumulator
            }
            val acc = ArrayList<ASTNode>()
            walkTree  (root, pred, acc)

            return acc
        }

        public fun  findIdentifiers(project: Project, key: String? = null): List<ASTNode>
        {
            val result = ArrayList<ASTNode>()
            val virtualFiles = FileBasedIndex.getInstance()!!
                    .getContainingFiles(
                    FileTypeIndex.NAME,
                    ELCodeFileType.INSTANCE,
                    GlobalSearchScope.allScope(project))

            for (virtualFile in virtualFiles)
            {
                val file = PsiManager.getInstance(project).findFile(virtualFile)
                if (file != null)
                    if (key != null)
                    {
                        val token = ELToken.fromIdentifier(key)
                        result.addAll(Util.CollectDescendants(file.getNode()!!, {(node)-> node.getElementType() == token }))
                    }
                    else
                    {
                        result.addAll(Util.CollectDescendants(file.getNode()!!, {(node)-> node.getElementType() is TokIdentifier }))
                    }
            }

            return result
        }


    }
}
