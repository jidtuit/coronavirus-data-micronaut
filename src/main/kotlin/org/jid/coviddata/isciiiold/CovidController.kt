package org.jid.coviddata.isciiiold

import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.Status
import kotlinx.coroutines.flow.toList
import org.jid.coviddata.isciiiold.Autonomy.MADRID
import org.jid.coviddata.isciiiold.CovidConstants.COVID_DATA_URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Controller("/covid")
class CovidController(private val service: CovidService){

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    @Status(HttpStatus.OK)
    suspend fun covidData(): Map<String, List<CovidDataResponse>> {
        val dataList = service.getCovidDataByAutonomyAndCountry()
                .toList()
        val firstDate = dataList[0].dataDate
        val responseList = dataList.map { it.toResponse(firstDate) }

        print("hola mundo")

        return responseList.groupBy { it.area }
    }

    @Get("/metadata")
    @Produces(MediaType.APPLICATION_JSON)
    @Status(HttpStatus.OK)
    suspend fun covidMetadataInfo(): CovidMetadataInfo {
        val metadata = service.getCovidMetadataInfo()
        val dateTime = metadata.dataFrom
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd  HH:mm:ss"))
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
                             val pcrPositiveCases:Long,
                             val pcrPositiveCasesInc:Long,
                             val testAcPositiveCases:Long,
                             val testAcPositiveCasesInc:Long
)

private fun CovidData.toResponse(firstElementDate: LocalDate): CovidDataResponse {
    // Format date for browser
    val isoDate = dataDate.toString()

    // Remove first element in daily variations
    val newTotalCasesInc = or0IfFirstElement(totalCasesInc, firstElementDate)
    var newHospitalCasesInc = or0IfFirstElement(hospitalCasesInc, firstElementDate)
    var newUciCasesInc = or0IfFirstElement(uciCasesInc, firstElementDate)
    val newDeathCasesInc = or0IfFirstElement(deathCasesInc, firstElementDate)
    val newPcrPositiveInc = or0IfFirstElement(pcrPositiveInc, firstElementDate)
    var newTestAcPositiveInc = or0IfFirstElement(testAcPositiveInc, firstElementDate)

    // Data exceptions to improve visualization
    newHospitalCasesInc = or0ByAreaAndDate(newHospitalCasesInc, MADRID, 2020, 4, 26)
    newUciCasesInc = or0ByAreaAndDate(newUciCasesInc, MADRID, 2020, 4, 26)
    newTestAcPositiveInc = or0ByAreaAndDate(newTestAcPositiveInc, MADRID, 2020, 4, 18)


    return CovidDataResponse(
            area, isoDate, totalCases, newTotalCasesInc, hospitalCases, newHospitalCasesInc, uciCases, newUciCasesInc,
            deathCases, newDeathCasesInc, pcrPositive, newPcrPositiveInc,
            testAcPositive, newTestAcPositiveInc
    )
}

private fun CovidData.or0ByAreaAndDate(other: Long, aut: Autonomy, year: Int, month: Int, day: Int): Long {
    return if(area == aut.iso && dataDate.isEqual(LocalDate.of(year, month, day))) 0 else other
}

private fun CovidData.or0IfFirstElement(other: Long, firstElementDate: LocalDate): Long {
    return if(dataDate.isEqual(firstElementDate)) 0 else other
}


data class CovidMetadataInfo(val notes: List<String>,
                             val dataDate: String,
                             val dataUrl: String = COVID_DATA_URL)