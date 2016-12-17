package org.nexivo.kt.vinyl.api.templating.dsl

import org.nexivo.kt.specifics.infixit.apply
import org.nexivo.kt.vinyl.api.templating.RecordTemplate

inline infix fun <T: RecordTemplate> (() -> T).`string template`(init: T.() -> Unit): String = (this() apply init).toString()
