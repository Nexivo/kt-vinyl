package org.nexivo.kt.vinyl.api

import org.nexivo.kt.specifics.equality.createHashCode
import org.nexivo.kt.vinyl.api.dsl.RecordClass

class RecordType(internal val record: RecordClass, val type: String, vararg fields: Field) {

    val fields: List<Field>

    private val _hashcode: Int by lazy { this.createHashCode(type, record, fields.asIterable()) }

    init {
        if (type.isBlank()) {
            throw IllegalArgumentException("'type' can not be blank!")
        }

        if (fields.isEmpty()) {
            throw IllegalArgumentException("you must define at least one 'field'!")
        }

        this.fields = fields.toList()
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true

        if (other?.javaClass != javaClass) return false

        other as RecordType

        return type   == other.type   &&
               record == other.record &&
               fields.containsAll(other.fields)
    }

    override fun hashCode(): Int    = _hashcode

    override fun toString(): String = "$type:\n   ${fields.joinToString("\n   ")}"
}
