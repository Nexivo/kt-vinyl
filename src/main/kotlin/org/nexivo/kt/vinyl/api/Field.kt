package org.nexivo.kt.vinyl.api

import org.nexivo.kt.specifics.equality.createHashCode

class Field(val type: FieldType, pattern: String = NO_PATTERN, val fieldName: String = NOT_CAPTURED) : Comparable<Field> {

    companion object {
        const val NOT_CAPTURED: String = ""
        const val NO_PATTERN:   String = ""
    }

    constructor(type: FieldType, fieldName: String)
            : this(type, NO_PATTERN, fieldName)

    private var _matcher: Regex?  = if (pattern.isNotEmpty()) Regex(pattern) else null

    private val _hashCode: Int by lazy { this.createHashCode(type, fieldName, _matcher?.pattern) }

    private val _capture: Boolean = fieldName.isNotBlank()

    val capture: Boolean = _capture

    fun matches(value: String): Boolean = type.isType(value) && looksLike(value)

    fun looksLike(value: String): Boolean = _matcher?.containsMatchIn(value) ?: true

    override fun compareTo(other: Field): Int = type.compareTo(other.type)

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        
        if (other?.javaClass != javaClass) return false

        @Suppress("UNCHECKED_CAST")
        (other as Field)

        return  type              == other.type      &&
                fieldName         == other.fieldName &&
                _matcher?.pattern == other._matcher?.pattern
    }

    override fun hashCode(): Int = _hashCode

    override fun toString(): String  = "$type [${if (capture) fieldName else "Not Captured"}, ${_matcher?.pattern ?: "No Pattern"}]"
}
