package org.nexivo.kt.vinyl.api.mappers

import org.nexivo.kt.vinyl.api.Record
import org.nexivo.kt.vinyl.api.dsl.Parser
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Mapper<out T> (parser: Parser<T>) : ReadOnlyProperty<Record, T> {

    private val _parser: Parser<T> = parser

    override fun getValue(thisRef: Record, property: KProperty<*>): T = _parser(thisRef.data[property.name]!!)
}