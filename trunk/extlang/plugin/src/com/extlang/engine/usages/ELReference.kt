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
import com.intellij.psi.PsiNamedElement

/**The reference should point at named element, means you NEED to use construction id ::= IDENT in your grammar, where id
is reserves to be the only parent of every identifier token. */
public class ELReference(public val _Element: PsiElement, public val _Range: TextRange, public var _Pointer: PsiElement): PsiReference
{
    public override fun getElement(): PsiElement? {
        return _Element
    }
    public override fun getRangeInElement(): TextRange? {
        return TextRange(0, _Element.getTextLength())
    }

    public override fun resolve(): PsiElement? {
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
 /*  //this method is bugged btw. Do not use without fixing it first.
  public override fun toString() : String
    {
       return "${(_Pointer.getNode()!! as ELASTNode).getName()!!} at ${_Pointer.getTextRange()} "
    } */
}
