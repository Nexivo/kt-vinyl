package org.nexivo.kt.vinyl.api.templating

import org.nexivo.kt.specifics.flow.ifSet
import org.nexivo.kt.vinyl.api.dsl.Validator
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class TemplateProperty<T>(private val validate: Validator<T>?, private val default: T?) : ReadWriteProperty<RecordTemplate, T> {

    constructor(): this(null, null)

    constructor(validate: Validator<T>): this(validate, null)

    constructor(default: T?): this(null, default)

    @Suppress("UNCHECKED_CAST") // TemplateProperty handles internal parameters map, low probability to cast incorrectly
    override fun getValue(thisRef: RecordTemplate, property: KProperty<*>): T =
        if (thisRef.parameters.containsKey(property.name) || default == null) {
            thisRef.parameters[property.name] as T
        } else {
            default
        }

    override fun setValue(thisRef: RecordTemplate, property: KProperty<*>, value: T) {

        validate ifSet {
            validate!!(value)
        }

        thisRef.parameters[property.name] = value
    }
}