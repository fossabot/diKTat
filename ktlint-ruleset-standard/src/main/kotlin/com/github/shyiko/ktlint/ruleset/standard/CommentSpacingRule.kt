package com.github.shyiko.ktlint.ruleset.standard

import com.github.shyiko.ktlint.core.Rule
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.PsiComment
import org.jetbrains.kotlin.com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import org.jetbrains.kotlin.com.intellij.psi.util.PsiTreeUtil

class CommentSpacingRule : Rule("comment-spacing") {

    override fun visit(
        node: ASTNode,
        autoCorrect: Boolean,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> Unit
    ) {
        if (node is LeafPsiElement && node is PsiComment && node.getText().startsWith("//")) {
            val prevLeaf = PsiTreeUtil.prevLeaf(node)
            if (prevLeaf !is PsiWhiteSpace && prevLeaf is LeafPsiElement) {
                emit(node.startOffset, "Missing space before end of line comment", true)
                if (autoCorrect) {
                    node.rawInsertBeforeMe(PsiWhiteSpaceImpl(" "))
                }
            }
            if (!node.getText().startsWith("// ")) {
                emit(node.startOffset, "Missing space after double slash in end of line comment", true)
                if (autoCorrect) {
                    node.rawReplaceWithText("// " + node.getText().removePrefix("//"))
                }
            }
        }
    }
}
