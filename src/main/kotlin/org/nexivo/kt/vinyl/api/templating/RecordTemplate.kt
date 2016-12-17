package org.nexivo.kt.vinyl.api.templating

abstract class RecordTemplate protected constructor(template: String, delimiter: String = " ", prefix: String = "", suffix: String = "") {

    private typealias Parameters = MutableMap<String, Any?>

    companion object {
        private val LEFT_OVER_PARAMETERS: Regex = Regex("\\[[^\\]]*\\]")
    }

    internal val parameters: Parameters = mutableMapOf()

    private val _template:  String = template
    private val _delimiter: String = delimiter
    private val _prefix:    String = prefix
    private val _suffix:    String = suffix

    override fun toString(): String {

        var result: String = _template

        parameters.forEach {

            parameter ->

            val value: String =
                when (parameter.value) {
                    null           -> ""
                    is String      -> parameter.value as String
                    is Array<*>    -> (parameter.value as Array<*>).joinToString(_delimiter)
                    is Iterable<*> -> (parameter.value as Iterable<*>).joinToString(_delimiter)
                    else           -> parameter.value.toString()
                }

            result = result.replace("[${parameter.key}]", value, true)
        }

        result = result.replace(LEFT_OVER_PARAMETERS, "")

        return "$_prefix$result$_suffix"
    }
}