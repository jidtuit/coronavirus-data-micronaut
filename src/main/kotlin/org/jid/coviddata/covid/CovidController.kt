package org.jid.coviddata.covid

import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.Status
import kotlinx.coroutines.flow.toList

@Controller("/covid")
class CovidController(private val service: CovidService){

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Status(HttpStatus.OK)
    suspend fun covidData(): List<CovidData> = service.getCovidData().toList()

}