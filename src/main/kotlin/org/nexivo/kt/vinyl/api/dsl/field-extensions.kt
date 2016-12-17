package org.nexivo.kt.vinyl.api.dsl

import org.nexivo.kt.vinyl.api.Field

infix fun String.`is a`(value: Field): Boolean = value.matches(this)

infix fun String.`looks like`(value: Field): Boolean = value.looksLike(this)
