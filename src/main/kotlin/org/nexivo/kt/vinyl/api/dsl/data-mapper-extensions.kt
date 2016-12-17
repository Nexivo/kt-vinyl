package org.nexivo.kt.vinyl.api.dsl

import org.nexivo.kt.vinyl.api.*
import org.nexivo.kt.vinyl.api.mappers.ArrayMapper
import org.nexivo.kt.vinyl.api.mappers.Mapper
import org.nexivo.kt.vinyl.api.mappers.dsl.ArrayMapper

val DataMapper.Boolean:  Mapper<Boolean>       by lazy { Mapper(::ParseBoolean) }

val DataMapper.Booleans: ArrayMapper<Boolean>  by lazy { ArrayMapper(::ParseBoolean) }

val DataMapper.Double:   Mapper<Double>        by lazy { Mapper(::ParseDouble) }

val DataMapper.Doubles:  ArrayMapper<Double>   by lazy { ArrayMapper(::ParseDouble) }

val DataMapper.Float:    Mapper<Float>         by lazy { Mapper(::ParseFloat) }

val DataMapper.Floats:   ArrayMapper<Float>    by lazy { ArrayMapper(::ParseFloat) }

val DataMapper.Integer:  Mapper<Int>           by lazy { Mapper(::ParseInt) }

val DataMapper.Integers: ArrayMapper<Int>      by lazy { ArrayMapper(::ParseInt) }

val DataMapper.Long:     Mapper<Long>          by lazy { Mapper(::ParseLong) }

val DataMapper.Longs:    ArrayMapper<Long>     by lazy { ArrayMapper(::ParseLong) }

val DataMapper.String:   Mapper<String>        by lazy { Mapper({ it }) }

val DataMapper.Strings:  ArrayMapper<String>   by lazy { ArrayMapper({ it }) }
