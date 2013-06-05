package com.extlang.parser

import com.extlang.BNFLexer
import com.extlang.grammar.BNFFile
import com.extlang.grammar.psi.BNFParser
import com.extlang.grammar.psi.BNFSimpleTypes
import com.intellij.lang.ASTNode
import com.intellij.lang.Language
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.extlang.engine.FixedSyntax
import com.intellij.openapi.fileEditor.impl.http.LocalFileFinder
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.extapi.psi.PsiFileBase
import com.extlang.engine.GrammarBuilder
import com.intellij.psi.tree.IElementType
import com.extlang.grammar.BNFElementType
import com.extlang.grammar.BNFLanguage

class BNFParserDefinition: ParserDefinition
{
    class object {
        val FILE: IFileElementType? = IFileElementType(Language.findInstance<BNFLanguage?>(BNFLanguage.INSTANCE!!.getClass()))
        val LOG = Logger.getInstance("#com.intellij.lang.properties.PropertiesParserDefinition")
        val TOKENS_WHITE_SPACE = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENT : IElementType = BNFElementType("COMMENT")
        val TOKENS_COMMENTS = TokenSet.create(COMMENT)

    }

    public override fun createLexer(project: Project?): Lexer {
        return BNFLexer()
    }
    public override fun createParser(project: Project?): PsiParser? {
        val parser = BNFParser()
        return parser
    }
    public override fun getFileNodeType(): IFileElementType? {
        return FILE
    }
    public override fun getWhitespaceTokens(): TokenSet {
        return TOKENS_WHITE_SPACE
    }
    public override fun getCommentTokens(): TokenSet {
        return TOKENS_COMMENTS
    }
    public override fun getStringLiteralElements(): TokenSet {
        return TokenSet.EMPTY
    }
    public override fun createElement(node: ASTNode?): PsiElement {
        return BNFSimpleTypes.Factory.createElement(node)!!
    }
    public override fun createFile(viewProvider: FileViewProvider?): PsiFile? {
        return  BNFFile(viewProvider!!)
    }
    public override fun spaceExistanceTypeBetweenTokens(left: ASTNode?, right: ASTNode?): ParserDefinition.SpaceRequirements? {
        return SpaceRequirements.MAY
    }


}