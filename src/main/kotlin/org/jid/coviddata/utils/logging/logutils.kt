package org.jid.coviddata.utils.logging

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.full.companionObject

interface Loggable

inline fun <reified T : Loggable> T.log(): Logger = getLogger(getClassForLogging(T::class.java))

inline fun getLogger(forClass: Class<*>): Logger = LoggerFactory.getLogger(forClass)

inline fun <T : Any> getClassForLogging(javaClass: Class<T>): Class<*> {
    return javaClass.enclosingClass?.takeIf {
        it.kotlin.companionObject?.java == javaClass
    } ?: javaClass
}