package com.extlang.engine.usages

import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode


public trait ELNamedElement : PsiNameIdentifierOwner
{

}

public abstract class ELNamedElementImpl(node:ASTNode) : ASTWrapperPsiElement(node)
{

}