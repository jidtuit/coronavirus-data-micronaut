package org.jid.coviddata.covid

import kotlinx.coroutines.flow.*
import org.jid.coviddata.covid.CovidConstants.COVID_DAYS_OF_DATA
import org.jid.coviddata.utils.logging.Loggable
import org.jid.coviddata.utils.logging.log
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Singleton

@Singleton
class CovidParser : Loggable {

    private val isoList: List<String> = Autonomy.values().map{ it.iso }

    fun parseData(data: String): Flow<CovidData> {
        val rows = data.lines()
                .drop(1)
                .filter { isValidData(it) }
                .mapNotNull { toCovidDataRow(it) }
                .filter { it.dataDate > LocalDate.now().minusDays(COVID_DAYS_OF_DATA) }

        val covidDataByArea: Map<String, List<CovidDataRow>> = rows.groupBy(CovidDataRow::area)

        val covidDataAggregated = covidDataByArea.values.flatMap { covidDataRowList ->

            covidDataRowList.sortedBy { row -> row.dataDate }
                    .fold(mutableListOf<CovidData>()) { acc, item ->
                        acc.add(row2Data(item, acc.lastOrNull()))
                        acc
                    }
        }

        val covidDataForCountry = calculateCountryValues(covidDataAggregated)

        return covidDataAggregated
                .union(covidDataForCountry)
                .asFlow()
    }

    private fun calculateCountryValues(autonomyData: List<CovidData>): List<CovidData> {
        return autonomyData.groupBy(CovidData::dataDate)
                .values
                .map { list ->
                    list.fold(CovidData("ALL", LocalDate.now())){ acc, item ->
                        CovidData(
                                acc.area, // aka "ALL"
                                item.dataDate,
                                item.totalCases + acc.totalCases,
                                item.totalCasesInc + acc.totalCasesInc,
                                item.hospitalCases + acc.hospitalCases,
                                item.hospitalCasesInc + acc.hospitalCasesInc,
                                item.uciCases + acc.uciCases,
                                item.uciCasesInc + acc.uciCasesInc,
                                item.deathCases + acc.deathCases,
                                item.deathCasesInc + acc.deathCasesInc,
                                item.recoveredCases + acc.recoveredCases,
                                item.recoveredCasesInc + acc.recoveredCasesInc
                        )
                    }
                }
    }


    private fun row2Data(today: CovidDataRow, dayBefore: CovidData? = null) : CovidData {
        return CovidData(today.area,
                today.dataDate,
                today.totalCases,
                today.totalCases - (dayBefore?.totalCases ?: 0) ,
                today.hospitalCases,
                today.hospitalCases - (dayBefore?.hospitalCases ?: 0),
                today.uciCases,
                today.uciCases - (dayBefore?.uciCases ?: 0),
                today.deathCases,
                today.deathCases - (dayBefore?.deathCases ?: 0),
                today.recoveredCases,
                today.recoveredCases - (dayBefore?.recoveredCases ?: 0)
        )

    }

    private fun toCovidDataRow(line: String): CovidDataRow? {
        val reg = line.split(",")

        return if(isValidRecord(reg)) {
            CovidDataRow(reg[0].trim(),
                    LocalDate.parse(reg[1], DateTimeFormatter.ofPattern("d/M/yyyy")),
                    orElse0(reg[2]),
                    orElse0(reg[3]),
                    orElse0(reg[4]),
                    orElse0(reg[5]),
                    orElse0(reg[6])
            )
        } else {
            log().info("Invalid record value: reg = ${reg}")
            null
        }
    }

    private fun isValidRecord(reg: List<String>): Boolean {
        if(reg.size > 7) {
            log().warn("There are more info in the current record. Please add new columns. Record: {}",reg)
        }
        return reg.size >= 7 && reg.isNotEmpty() && reg[0].isNotBlank() && reg[1].isNotBlank()
    }

    private fun orElse0(text: String): Long {
        return if(text.isNotBlank()) { text.toLong() } else 0;
    }

    private fun isValidData(line: String):Boolean = line.isNotEmpty() &&
            line.length > 3 && line.substring(0,2) in isoList && line[2] == ','

}

private data class CovidDataRow(val area: String,
                        val dataDate: LocalDate,
                        val totalCases:Long = 0,
                        val hospitalCases:Long = 0,
                        val uciCases:Long = 0,
                        val deathCases:Long = 0,
                        val recoveredCases: Long = 0
)
