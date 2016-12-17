package org.nexivo.kt.vinyl.api

import org.nexivo.kt.vinyl.api.dsl.DataMappings
import org.nexivo.kt.vinyl.api.dsl.FormatHandler

abstract class BaseRecord(override val data:      DataMappings,
                                       delimiter: String = "\n",
                                       formatter: FormatHandler = { "${it.key}:${it.value}"})
    : Record {

    private val _string: String by lazy { data.map(formatter).joinToString(delimiter) }

    override fun toString(): String = _string

    override fun equals(other: Any?): Boolean {

        if (this === other) return true

        if (other?.javaClass != javaClass) return false

        other as BaseRecord

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int {
        return data.hashCode()
    }
}