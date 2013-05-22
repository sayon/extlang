package com.extlang.parser

import com.extlang.ELLexer
import com.extlang.engine.ELLanguage
import com.extlang.ELFile
import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.impl.source.tree.CompositePsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.extlang.engine.ELParser
import com.extlang.engine.ELASTNode

class ELParserDefinition: ParserDefinition
{
    class object {
        val FILE: IFileElementType? = IFileElementType(Language.findInstance<ELLanguage?>(ELLanguage.INSTANCE!!.getClass()))
        val LOG = Logger.getInstance("#com.intellij.lang.properties.PropertiesParserDefinition")
        val TOKENS_WHITE_SPACE = TokenSet.create(TokenType.WHITE_SPACE)
        val TOKENS_COMMENTS = TokenSet.EMPTY
    }

    public override fun createLexer(project: Project?): Lexer {
        return ELLexer()
    }
    public override fun createParser(project: Project?): PsiParser? {
        return ELParser()
    }
    public override fun getFileNodeType(): IFileElementType? {
        return FILE
    }
    public override fun getWhitespaceTokens(): TokenSet {
        return TOKENS_WHITE_SPACE
    }
    public override fun getCommentTokens(): TokenSet {
        return TokenSet.EMPTY
    }
    public override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY //No string literals allowed atm. Todo implement string literals
    }
    public override fun createElement(node: ASTNode?): PsiElement {
        return ELASTNode(node!!)

    }
    public override fun createFile(viewProvider: FileViewProvider?): PsiFile? {
        return ELFile(viewProvider!!)
    }
    public override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements? {
        return SpaceRequirements.MAY
    }


}