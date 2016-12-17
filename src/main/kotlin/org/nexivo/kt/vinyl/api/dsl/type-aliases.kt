package org.nexivo.kt.vinyl.api.dsl

import org.nexivo.kt.vinyl.api.DataMapper
import org.nexivo.kt.vinyl.api.Record

typealias FormatHandler = (DataMapping) -> String

typealias DataMapping   = Map.Entry<String, String>

typealias MapperClass   = (DataMappings) -> DataMapper

typealias DataMappings  = Map<String, String>

typealias RecordClass   = (DataMapper) -> Record

typealias Validator<T>  = (T) -> Unit

typealias Parser<T>     = (String) -> T