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
import com.intellij.psi.PsiReference
import javax.naming.OperationNotSupportedException

public class ELReference(public val _Element: PsiElement, public val _Range: TextRange, public var _Pointer: PsiElement): PsiReference
{


    public override fun getElement(): PsiElement? {
        return _Element
    }
    public override fun getRangeInElement(): TextRange? {
        return TextRange(0, _Element.getTextLength())
    }

    public override fun resolve(): PsiElement? {
        //val identifiers = Util.findIdentifiers(Element.getProject(), key)
        // return identifiers[0].getPsi()
        return _Pointer
    }
    public override fun getCanonicalText(): String {
        return _Element.getText()!!
    }
    public override fun handleElementRename(newElementName: String?): PsiElement? {
        return _Element
    }
    public override fun bindToElement(element: PsiElement): PsiElement? {
        throw OperationNotSupportedException()
    }
    public override fun isReferenceTo(element: PsiElement?): Boolean {
        return resolve() == element
    }
    public override fun getVariants(): Array<Any> {
        return array()
    }
    public override fun isSoft(): Boolean {
        return false
    }
}

/*

  public fun getKey_(): String ?
    {
        val child = Element.getFirstChild()
        if (child != null )
        {
            val tok = child.getNode()!!.getElementType()
            if (tok != null && tok is TokIdentifier)
                return tok.Name
        }
        return null
    }


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


}            */

/*
open class ELReference() : (element: PsiElement, textRange: TextRange):
PsiReferenceBase(element, textRange){

public override fun getElement() : PsiElement? {
return null
}
public override fun getRangeInElement() : TextRange? {
return null
}
public override fun resolve() : PsiElement? {
return null
}
public override fun getCanonicalText() : String {
return null
}
public override fun handleElementRename(newElementName : String?) : PsiElement? {
return null
}
public override fun bindToElement(element : PsiElement) : PsiElement? {
return null
}
public override fun isReferenceTo(element : PsiElement?) : Boolean {
return false
}
public override fun getVariants() : Array<Any?> {
return Array<Any?>(0)
}
public override fun isSoft() : Boolean {
return false
}


}
                       */