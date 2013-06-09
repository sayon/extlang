package com.extlang.engine.usages

import com.intellij.psi.PsiReferenceBase
import com.extlang.engine.parser.ELASTNode
import com.intellij.psi.PsiElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.ResolveResult
import com.intellij.openapi.vfs.newvfs.NewVirtualFileSystem
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.psi.search.FileTypeIndex
import com.extlang.engine.model.TokIdentifier
import com.extlang.util.Util
import java.util.ArrayList
import com.intellij.psi.PsiElementResolveResult
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.extlang.Icons

public class ELReference(element: PsiElement, textRange: TextRange):
PsiReferenceBase<PsiElement>(element, textRange)
{
    public override fun resolve(): PsiElement? {
        val project = myElement!!.getProject()
        val key = (myElement?.getFirstChild()?.getNode()!!.getElementType() as TokIdentifier).Name
        val idents = Util.findIdentifiers(project, key)
        if (idents.size == 0) return null
        val result = idents[0].getPsi()!!.getFirstChild();
        return result
    }
    public override fun getVariants(): Array<Any> {
       /* val project = myElement!!.getProject()
        val identifiers = Util.findIdentifiers(project)
        val variants = ArrayList<Any>()
        for( id in identifiers)
        {
            val ident = id as ELASTNode
            if (ident.Name != null && ident.Name!!.length() > 0)
            {
                variants.add(LookupElementBuilder.create(ident)!!
                        .withIcon(Icons.CODEFILE)!!
                        .withTypeText(ident.getContainingFile()!!.getName()) as Any
                )
            }
        }
        return variants.toArray() as Array<Any>         */

        val project = myElement!!.getProject()
        val identifiers = Util.findIdentifiers(project)
        val variants = Array<Any>(identifiers.size, {(i) -> (identifiers.get(i) as ELASTNode ).getName() as Any} )
        return variants
    }
    /*public fun isReferenceTo(other:PsiElement) : Boolean
    {
        val tok = other.getNode()!!.getElementType()
        if (tok is TokIdentifier)
            {
                return tok.Name == key
            }
        val tokchild = other.getNode()!!.getFirstChildNode()?.getElementType()
        if( tokchild != null && tokchild is TokIdentifier)
            {
                return tokchild.Name == key
            }

        return false
    }
                     */
}