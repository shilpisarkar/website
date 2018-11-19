package com.github.wakingrufus.website.lib.code

import com.github.wakingrufus.website.lib.css
import kotlinx.css.Color
import kotlinx.css.FontStyle
import kotlinx.css.FontWeight
import kotlinx.css.em
import kotlinx.css.properties.TextDecoration
import kotlinx.css.properties.TextDecorationLine
import kotlinx.html.*

fun DIV.sampleCode(block: CODE.() -> Unit) {
    return pre {
        code {
            style = css {
                fontSize = 1.2.em
                fontWeight = FontWeight.bold
            }
            block(this)
        }
    }
}

fun CODE.keyword(text: String) {
    return span {
        style = css {
            color = Color("#CC7832")
        }
        +text
    }
}

fun CODE.it() {
    return span {
        style = css {
            fontWeight = FontWeight("999")
        }
        +"it"
    }
}

fun CODE.string(string: String) {
    return span {
        style = css {
            color = Color("#6A8759")
        }
        +"\""
        +string
        +"\""
    }
}

fun CODE.number(value: Number) {
    return span {
        style = css {
            color = Color("#6897BB")
        }
        +value.toString()
    }
}

fun CODE.block(indentation: Int = 0, inline: Boolean = false, code: BLOCK.() -> Unit) {
    BLOCK(indentation = indentation, inline = inline).apply(code)(this)
}

fun CODE.call(name: String,
              argsOnDifferentLines: Boolean = false,
              baseIndentation: Int = 0,
              block: CALL.() -> Unit = {}) {
    this.apply {
        CALL(name = name,
                argsOnDifferentLines = argsOnDifferentLines,
                baseIndentation = baseIndentation)
                .apply(block)(this)
    }
}

fun CODE.constructor(name: String,
                     argsOnDifferentLines: Boolean = false,
                     baseIndentation: Int = 0,
                     block: CALL.() -> Unit) {
    this.apply {
        CALL(name = name, argsOnDifferentLines = argsOnDifferentLines, baseIndentation = baseIndentation)
                .apply(block)(this)
    }
}

fun CODE.on(subject: CODE.() -> Unit, block: SUBJECT.() -> Unit = {}) {
    SUBJECT(subject).apply(block)(this)
}

fun CODE.functionName(text: String) {
    return span {
        style = css {
            color = Color("#FFC66D")
        }
        +text
    }
}

fun CODE.propertyName(text: String) {
    return span {
        style = css {
            fontStyle = FontStyle.italic
            color = Color("#9876AA")
        }
        +text
    }
}

fun CODE.variablePropertyName(text: String) {
    return span {
        style = css {
            fontStyle = FontStyle.italic
            color = Color("#9876AA")
            textDecoration = TextDecoration(lines = setOf(TextDecorationLine.underline),
                    color = Color("#BCA5C4"))
        }
        +text
    }
}

fun CODE.comment(text: String) {
    return span {
        style = css {
            color = Color("#808080")
        }
        +"// "
        +text
    }
}

fun CODE.declareClass(modifiers: List<String> = emptyList(),
                      name: String,
                      superClass: String? = null,
                      propsOnSeparateLines: Boolean = true,
                      block: CLASS.() -> Unit = {}) {
    CLASS(modifiers = modifiers, name = name, superClass = superClass, propsOnSeparateLines = propsOnSeparateLines)
            .apply(block)(this)
}

fun CODE.dataClass(name: String,
                   propsOnSeparateLines: Boolean = true,
                   block: CLASS.() -> Unit = {}) {
    this.apply {
        CLASS(modifiers = listOf("data"), propsOnSeparateLines = propsOnSeparateLines, name = name)
                .apply(block)(this)
    }
}

fun CODE.annotationClass(name: String,
                   propsOnSeparateLines: Boolean = true,
                   block: CLASS.() -> Unit = {}) {
    this.apply {
        CLASS(modifiers = listOf("annotation"), propsOnSeparateLines = propsOnSeparateLines, name = name)
                .apply(block)(this)
    }
}

fun CODE.declareFunction(name: String,
                         returnType: String? = null,
                         argsOnSeparateLines: Boolean = false,
                         indentation: Int = 0,
                         extentionOf: String? = null,
                         block: FUNCTION.() -> Unit) {
    this.apply {
        FUNCTION(name = name,
                returnType = returnType,
                paramsOnSeparateLines = argsOnSeparateLines,
                extensionOf = extentionOf,
                indentation = indentation)
                .apply(block)(this)
        +"\n"
    }
}

fun CODE.declareFunctionExpression(name: String,
                                   returnType: String? = null,
                                   argsOnSeparateLines: Boolean = true,
                                   parameters: List<PARAMETER> = listOf(),
                                   expression: CODE.() -> Unit) {
    this.apply {
        FUNCTION(name = name, returnType = returnType, paramsOnSeparateLines = argsOnSeparateLines)
                .apply {
                    this.parameters = parameters
                    expression(expression)
                }(this)
    }
}

fun CODE.parameterName(text: String) {
    return span {
        style = css {
            color = Color("#467CDA")
        }
        +text
    }
}

fun CODE.declareReturn(block: CODE.() -> Unit) {
    this.apply {
        keyword("return ")
        block.invoke(this)
    }

}

fun CODE.declareProperty(modifier: String? = null, name: String, type: String? = null, value: (CODE.() -> Unit)? = null) {
    PROPERTY(modifier = modifier, name = name, type = type)
            .apply { value?.let { value(it) } }
            .invoke(this)
}

fun CODE.indent(level: Int) {
    +"  ".repeat(level)
}

@HtmlTagMarker
class ARGUMENT(val name: String?, val valueBlock: CODE.() -> Unit) {

    operator fun invoke(code: CODE) {
        code.apply {
            this@ARGUMENT.name?.let {
                parameterName(it)
                parameterName(" = ")
            }
            code.apply(this@ARGUMENT.valueBlock)
        }
    }
}

val keywords = listOf("var", "val", "return", "fun")

fun CODE.kotlinLine(line: String) {
    if (line.trimStart().startsWith("fun ")) {
        line.trimStart().removePrefix("fun ").let {
            keyword("fun ")
            it.substringBefore("{").substringBefore("(").let { functionName ->
                functionName(functionName)
                it.removePrefix(functionName).let {
                    if (it.startsWith("(")) {
                        functionParams(it.substringAfter("(").substringBefore(")"))
                    }
                    +it.substringAfter(")")
                }

            }

        }
    } else if (line.trimStart().startsWith("val")) {
        line.trimStart().removePrefix("val ").let {
            keyword("val ")

        }
    }
//                line.split(" ").map {
//                    if (keywords.contains(it)) keyword(it) else +it
//                }
    +"\n"
}

fun CODE.functionParams(params: String) {
    +"("
    if (params.length > 80) {
        +"\n"
        +" ".repeat(4)
    }
    +params
    if (params.length > 80) {
        +"\n"
    }
    +")"
}

fun CODE.functionBody(body: String) {
    +"{\n"

    +"}"
}

fun CODE.rawKotlin(raw: String) {
    if (raw.contains("fun ")) {
        rawKotlin(raw.substringBefore("fun "))
        keyword("fun ")
        rawKotlin(raw.substringAfter("fun "))
    } else {
        +raw
    }
}

fun DIV.kotlin(code: String) {
    return pre {
        code {
            style = css {
                fontSize = 1.2.em
                fontWeight = FontWeight.bold
            }
            rawKotlin(code)
        }
    }
}

fun CODE.whenExpression(indentation: Int = 0, whenBlock: WHEN.() -> Unit) {
    this.apply { WHEN(indentation = indentation).apply(whenBlock)(this) }
}
