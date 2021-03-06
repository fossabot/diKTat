package org.cqfn.diktat.ruleset.utils

import com.pinterest.ktlint.core.ast.ElementType
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.kdoc.parser.KDocKnownTag
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtParameterList

fun ASTNode.hasParameters(): Boolean {
    checkNode(this)
    val argList = this.argList()
    return argList != null && argList.hasChildOfType(ElementType.VALUE_PARAMETER)
}

fun ASTNode.parameterNames(): Collection<String?>? {
    checkNode(this)
    return (this.argList()?.psi as KtParameterList?)
        ?.parameters?.map { (it as KtParameter).name }
}

fun ASTNode.hasParametersKDoc(): Boolean {
    checkNode(this)
    val kDocTags = this.kDocTags()
    return kDocTags != null && kDocTags.hasKnownKDocTag(KDocKnownTag.PARAM)
}

private fun ASTNode.argList(): ASTNode? {
    checkNode(this)
    return this.getFirstChildWithType(ElementType.VALUE_PARAMETER_LIST)
}

private fun checkNode(node: ASTNode) =
    require(node.elementType == ElementType.FUN) {
        "This utility method operates on nodes of type ElementType.FUN only"
    }
