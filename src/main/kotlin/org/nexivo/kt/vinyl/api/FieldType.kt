package org.nexivo.kt.vinyl.api

import org.nexivo.kt.specifics.equality.createHashCode

abstract class FieldType(val name: String, pattern: String, val priority: Int = 1): Comparable<FieldType> {

    companion object {

        private const val PATTERN_STRING:  String = "[A-Za-z]+"
        private const val PATTERN_NUMBER:  String = "^\\s*(?:\\+|-)?\\s*\\d+(?:\\.\\d+(?:[+-]?e|E\\d+)?)?\\s*$"
        private const val PATTERN_INTEGER: String = "^\\s*(?:\\+|-)?\\s*\\d+\\s*$"

        val STRING_FIELD:  FieldType = object: FieldType("string",  PATTERN_STRING,  priority = 0) {}

        val DOUBLE_FIELD:  FieldType = object: FieldType("double",  PATTERN_NUMBER,  priority = 2) {}

        val FLOAT_FIELD:   FieldType = object: FieldType("float",   PATTERN_NUMBER,  priority = 2) {}

        val INTEGER_FIELD: FieldType = object: FieldType("integer", PATTERN_INTEGER, priority = 1) {}

        val LONG_FIELD:    FieldType = object: FieldType("long",    PATTERN_INTEGER, priority = 1) {}
    }

    private val _matcher: Regex = Regex(pattern)

    private val _hashCode: Int by lazy { this.createHashCode(name, _matcher.pattern) }

    init {
        if (pattern.isBlank()) {
            throw IllegalArgumentException("'pattern' can not be blank!")
        }
    }

    fun isType(value: String): Boolean = _matcher.containsMatchIn(value)

    open fun adjust(value: String): String = value

    override fun compareTo(other: FieldType): Int = this.priority.compareTo(other.priority)

    @Suppress("UNCHECKED_CAST")
    override fun equals(other: Any?): Boolean {

        if (this === other) return true

        if (other?.javaClass != javaClass) return false

        other as FieldType

        return name             == other.name     &&
               _matcher.pattern == other._matcher.pattern
    }

    override fun hashCode(): Int = _hashCode

    override fun toString(): String  = "$name '${_matcher.pattern}'"
}