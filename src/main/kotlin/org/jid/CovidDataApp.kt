package org.jid

import io.micronaut.runtime.Micronaut

object CovidDataApp {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
                .packages("org.jid")
                .mainClass(CovidDataApp.javaClass)
                .start()
    }
}