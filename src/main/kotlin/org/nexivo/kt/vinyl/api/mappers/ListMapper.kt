package org.nexivo.kt.vinyl.api.mappers

import org.nexivo.kt.vinyl.api.ARRAY_SPLITTER
import org.nexivo.kt.vinyl.api.Record
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ListMapper<out T> (parser: (String)-> T, splitter: String = ARRAY_SPLITTER) : ReadOnlyProperty<Record, List<T>> {

    private val _parser:   (String) -> T = parser
    private val _splitter: Regex         = Regex(splitter)

    override fun getValue(thisRef: Record, property: KProperty<*>): List<T> {

        val result: MutableList<T> = mutableListOf()

        result.addAll(thisRef.data[property.name]!!.split(_splitter).map{ _parser(it) })

        return result.toList()
    }
}
