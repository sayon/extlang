package com.extlang.engine.parser;

import com.extlang.engine.model.TokIdentifier;
import com.extlang.engine.usages.UsagesUtil;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class ELASTNode extends ASTWrapperPsiElement implements PsiNamedElement {
    public ELASTNode(@NotNull ASTNode node) {
        super(node);
    }

    public String Name = "";

    @Override
    public String getName() {
        final PsiElement child = getFirstChild();
        if (child == null || !(child.getNode().getElementType() instanceof TokIdentifier)) return null;
        final TokIdentifier tid = (TokIdentifier) (child.getNode().getElementType());
        return tid.getName();
    }


    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {

     /*   Name = name;
        return this;*/
        throw new IncorrectOperationException("Set name is not implemented");
    }

    @Override
    public PsiReference[] getReferences() {
        if (getName() != null && getName().length() > 0)

        {
            final PsiReference firstReference = UsagesUtil.object$.firstIdentifierReference(this);
            if (firstReference != null) return new PsiReference[]{firstReference};
        }
        return PsiReference.EMPTY_ARRAY;
    }
}



