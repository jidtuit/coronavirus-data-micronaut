package org.jid.prueba1

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.Status
import kotlinx.coroutines.*

@Controller("/prueba1")
class Prueba1Controller {

    @Get
    @Produces("text/html")
    @Status(HttpStatus.OK)
    fun sayHello(): String {
        return "hola mundo reloaded!!"
    }

}