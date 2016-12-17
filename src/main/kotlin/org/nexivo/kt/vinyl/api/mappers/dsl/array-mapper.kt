package org.nexivo.kt.vinyl.api.mappers.dsl

import org.nexivo.kt.vinyl.api.ARRAY_SPLITTER
import org.nexivo.kt.vinyl.api.Record
import org.nexivo.kt.vinyl.api.dsl.Parser
import org.nexivo.kt.vinyl.api.mappers.ArrayMapper
import kotlin.reflect.KProperty

inline fun <reified T> ArrayMapper (noinline parser: Parser<T>, splitter: String = ARRAY_SPLITTER): ArrayMapper<T> {

    val result = object : ArrayMapper<T> {

        private var _parser:   Parser<T>? = null
        private val _splitter: Regex      = Regex(splitter)

        override fun getValue(thisRef: Record, property: KProperty<*>): Array<T> {

            return thisRef.data[property.name]!!.split(_splitter).map { _parser!!.invoke(it) }.toTypedArray()
        }

        internal fun setup(parser: Parser<T>): Unit {
            _parser = parser
        }
    }

    result.setup(parser)

    return result
}
