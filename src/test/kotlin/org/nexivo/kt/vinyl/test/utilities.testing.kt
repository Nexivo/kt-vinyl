package org.nexivo.kt.vinyl.test

inline fun <reified T : Any> className() = T::class.java.simpleName!!

inline fun <reified T> throws(message: String? = null, block: () -> Unit)
        where T : Throwable
{
    try {
        block()
    } catch (ex: Throwable) {

        if (ex is T && (message == null || ex.message == message)) { return@throws }

        if (ex is T) {
            throw AssertionError("Unexpected message was received; [${ex.message}] != [$message]!", ex)
        }

        throw AssertionError("Expected ${className<T>()} was not thrown!", ex)
    }

    throw AssertionError("Expected ${className<T>()} was NOT Thrown!")
}
