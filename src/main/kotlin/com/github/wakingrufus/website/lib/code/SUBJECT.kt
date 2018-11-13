package com.github.wakingrufus.website.lib.code

import kotlinx.html.CODE
import kotlinx.html.HtmlTagMarker

@HtmlTagMarker
class SUBJECT(val subject: CODE.() -> Unit) {

    var calls: List<CODE.() -> Unit> = ArrayList()
    var operator: String = "."

    fun call(name: String, argsOnDifferentLines: Boolean = false, baseIndentation: Int = 0, block: CALL.() -> Unit = {}) {
        calls += {
            +this@SUBJECT.operator
            CALL(name = name, argsOnDifferentLines = argsOnDifferentLines, baseIndentation = baseIndentation)
                    .apply(block)(this)
        }
    }

    fun property(name: String){
        calls += {
            +this@SUBJECT.operator
            propertyName(name)
        }
    }

    fun assignment(){
        operator = " = "
    }

    fun nullSafe(){
        operator = "?."
    }
    fun standard(){
        operator = "."
    }
    fun breakAndCall(name: String,
                     argsOnDifferentLines: Boolean = true,
                     baseIndentation: Int = 0,
                     block: CALL.() -> Unit) {
        calls += {
            +"\n"
            indent(baseIndentation + 2)
            +this@SUBJECT.operator
            CALL(name = name, argsOnDifferentLines = argsOnDifferentLines, baseIndentation = baseIndentation)
                    .apply(block)(this)
        }
    }

    operator fun invoke(code: CODE) {
        code.apply {
            this@SUBJECT.subject(code)
            this@SUBJECT.calls.forEach {
                it(this)
            }
        }
    }
}
