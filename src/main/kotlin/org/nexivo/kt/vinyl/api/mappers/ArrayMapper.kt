package org.nexivo.kt.vinyl.api.mappers

import org.nexivo.kt.vinyl.api.Record
import kotlin.properties.ReadOnlyProperty

interface ArrayMapper<T> : ReadOnlyProperty<Record, Array<T>>