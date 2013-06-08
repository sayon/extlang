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
PsiReferenceBase<PsiElement>(element, textRange),
PsiPolyVariantReference
{
    public override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = myElement!!.getProject()
        val tok = myElement!!.getNode()!!.getFirstChildNode()!!.getElementType()!!
        if (tok is TokIdentifier)
        {
            val key = tok.Name
            val idents = Util.findIdentifiers(project, key)
            val result = Array<ResolveResult>(idents.size,
                    {(i) -> com.intellij.psi.PsiElementResolveResult(idents.get(i).getPsi()!!, true) })
            return result
        }
        else return array()
    }

    public override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false);
        return if (resolveResults.size >= 1) resolveResults[0].getElement() else null
    }
    public override fun getVariants(): Array<Any> {
        val project = myElement!!.getProject()
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
        return variants.toArray() as Array<Any>
    }


}
/*public Object[] getVariants() {
     Project project = myElement.getProject();
     List<SimpleProperty> properties = SimpleUtil.findProperties(project);
     List<LookupElement> variants = new ArrayList<LookupElement>();
     for (final SimpleProperty property : properties) {
         if (property.getKey() != null && property.getKey().length() > 0) {
             variants.add(LookupElementBuilder.create(property).
                     withIcon(SimpleIcons.FILE).
                     withTypeText(property.getContainingFile().getName())
             );
         }
     }
     return variants.toArray();
 }*/



/*
/* private var k: String = "";

 {
     val x = 1
     val toktype = element.getNode()!!.getElementType()
     val text = element.getText()!!
    /* key = if (toktype is TokIdentifier)
         toktype.Name
     else
         element.getText()!!.substring(textRange.getStartOffset(), textRange.getEndOffset());*/
     if (toktype is TokIdentifier)
         k = toktype.Name
     else
         k = element.getText()!!
 }

 public override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
     val project = myElement!!.getProject()
     val idents = Util.findIdentifiers(project, k)
     val results = ArrayList<ResolveResult>()
     var result: Array<ResolveResult> = array()
     idents.map { i-> PsiElementResolveResult(i) }.toArray<ResolveResult>(result)
     return result
 }
 public override fun resolve(): PsiElement? {
     val resolveResults = multiResolve(false);
     return if (resolveResults.size == 1) resolveResults[0].getElement() else null
 }
 public override fun getVariants(): Array<Any> {
     val project = myElement!!.getProject()
     val identifiers = Util.findIdentifiers(project)
     val variants = ArrayList<Any>()
     for( ident in identifiers)
         if (ident.Name != null && ident.Name!!.length()>0)
             {
                 variants.add(LookupElementBuilder.create(ident)!!
                         .withIcon(Icons.CODEFILE)!!
                         .withTypeText(ident.getContainingFile()!!.getName()) as Any
                 )
             }
     return variants.toArray() as Array<Any>
 }


}
/*public Object[] getVariants() {
     Project project = myElement.getProject();
     List<SimpleProperty> properties = SimpleUtil.findProperties(project);
     List<LookupElement> variants = new ArrayList<LookupElement>();
     for (final SimpleProperty property : properties) {
         if (property.getKey() != null && property.getKey().length() > 0) {
             variants.add(LookupElementBuilder.create(property).
                     withIcon(SimpleIcons.FILE).
                     withTypeText(property.getContainingFile().getName())
             );
         }
     }
     return variants.toArray();
 }*/


       }*/