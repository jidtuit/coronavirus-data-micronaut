package org.jid.coviddata.covid

import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.Status
import kotlinx.coroutines.flow.toList
import org.jid.coviddata.covid.CovidConstants.COVID_DATA_URL
import org.jid.coviddata.covid.CovidConstants.COVID_TIMEZONE
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Controller("/covid")
class CovidController(private val service: CovidService){

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Status(HttpStatus.OK)
    suspend fun covidData(): Map<String, List<CovidDataResponse>> {
        val dataList: List<CovidDataResponse> = service.getCovidDataByAutonomyAndCountry()
                .toList()
                .map { it.toResponse() }

        return dataList.groupBy { it.area }
    }

    @Get("/metadata")
    @Produces(MediaType.APPLICATION_JSON)
    @Status(HttpStatus.OK)
    suspend fun covidMetadataInfo(): CovidMetadataInfo {
        val metadata = service.getCovidMetadataInfo()
        val dateTime = metadata.dataFrom
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss"))
        return CovidMetadataInfo(metadata.notes.toList(), dateTime)
    }

}

data class CovidDataResponse(val area: String,
                             val dataDate: String,
                             val totalCases:Long,
                             val totalCasesInc:Long,
                             val hospitalCases:Long,
                             val hospitalCasesInc:Long,
                             val uciCases:Long,
                             val uciCasesInc:Long,
                             val deathCases:Long,
                             val deathCasesInc:Long,
                             val recoveredCases: Long,
                             val recoveredCasesInc: Long
)

private fun CovidData.toResponse(): CovidDataResponse {
    val isoDate = dataDate.toString()

    return CovidDataResponse(
            area, isoDate, totalCases, totalCasesInc, hospitalCases, hospitalCasesInc, uciCases, uciCasesInc,
            deathCases, deathCasesInc, recoveredCases, recoveredCasesInc
    )
}


data class CovidMetadataInfo(val notes: List<String>,
                             val dataDate: String,
                             val dataUrl: String = COVID_DATA_URL)