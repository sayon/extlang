package com.extlang.engine.parser;

import com.extlang.engine.model.ELToken;
import com.extlang.engine.usages.ELNamedElement;
import com.extlang.engine.usages.UsagesUtil;
import com.extlang.util.Util;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.util.IncorrectOperationException;
import com.extlang.engine.model.TokIdentifier;
import com.intellij.psi.impl.source.tree.*;
import com.intellij.psi.tree.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;


public class ELASTNode extends ASTWrapperPsiElement implements PsiNamedElement {
    public ELASTNode(@NotNull ASTNode node) {
        super(node);
    }

    public String Name = "";

    /**
     * Renames the element.
     *
     * @param name the new element name.
     * @return the element corresponding to this element after the rename (either <code>this</code>
     *         or a different element if the rename caused the element to be replaced).
     * @throws com.intellij.util.IncorrectOperationException
     *          if the modification is not supported or not possible for some reason.
     */
    @Override
    public PsiElement setName(@NonNls @NotNull String name) throws IncorrectOperationException {
        PsiElement elem = getFirstChild();  //findChildByFilter(ELToken.object.$instance.AllIdentifiers());
        if (elem != null && (elem.getNode().getElementType() instanceof TokIdentifier)) {
            final ELASTNode toknode = (ELASTNode) (elem.getNode());
            toknode.Name = name;
        }
        return this;
    }

    @Override
    public String getName() {
        final PsiElement elem;
        elem = getFirstChild();
        if (elem != null) {
            final IElementType et = ((LeafPsiElement) elem).getElementType();
            if (et instanceof TokIdentifier)
                return ((TokIdentifier) et).getName();
        }
        return null;
    }


    @Override
    public PsiReference getReference() {
        PsiReference[] references = getReferences();
        if (references.length > 0) return references[0];
        else return null;
    }

    @Override
    public PsiReference[] getReferences() {
        return UsagesUtil.object.$instance.getReferencesByElement(this, new ProcessingContext());
    }

}