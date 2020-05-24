package org.jid.coviddata.covid

import kotlinx.coroutines.flow.*
import org.jid.coviddata.covid.CovidConstants.CD_CCAA_INDEX
import org.jid.coviddata.covid.CovidConstants.CD_DATE_INDEX
import org.jid.coviddata.covid.CovidConstants.CD_DEATH_INDEX
import org.jid.coviddata.covid.CovidConstants.CD_HOSPITAL_CASES_INDEX
import org.jid.coviddata.covid.CovidConstants.CD_PCR_POSITIVE_INDEX
import org.jid.coviddata.covid.CovidConstants.CD_TEST_AC_POSITIVE_INDEX
import org.jid.coviddata.covid.CovidConstants.CD_TOTAL_CASES_INDEX
import org.jid.coviddata.covid.CovidConstants.CD_UCI_CASES_INDEX
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

    fun parseMetadataInfo(data: String): Flow<String> {
        return data.lines()
                .drop(1) // Header
                .filter { !isValidData(it) }
                .filter { it.isNotBlank() }
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
                                item.pcrPositive + acc.pcrPositive,
                                item.pcrPositiveInc + acc.pcrPositiveInc,
                                item.testAcPositive + acc.testAcPositive,
                                item.testAcPositiveInc + acc.testAcPositiveInc
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
                today.pcrPositive,
                today.pcrPositive - (dayBefore?.pcrPositive ?: 0),
                today.testAcPositive,
                today.testAcPositive - (dayBefore?.testAcPositive ?: 0)
        )

    }

    private fun toCovidDataRow(line: String): CovidDataRow? {
        val reg = line.split(",")

        return if(isValidRecord(reg)) {
            CovidDataRow(reg[CD_CCAA_INDEX].trim(),
                    LocalDate.parse(reg[CD_DATE_INDEX], DateTimeFormatter.ofPattern("d/M/yyyy")),
                    orElse(reg[CD_TOTAL_CASES_INDEX], orElse0(reg[CD_PCR_POSITIVE_INDEX]) + orElse0(reg[CD_TEST_AC_POSITIVE_INDEX])),
                    orElse0(reg[CD_HOSPITAL_CASES_INDEX]),
                    orElse0(reg[CD_UCI_CASES_INDEX]),
                    orElse0(reg[CD_DEATH_INDEX]),
                    orElse0(reg[CD_PCR_POSITIVE_INDEX]),
                    orElse0(reg[CD_TEST_AC_POSITIVE_INDEX])
            )
        } else {
            log().warn("Invalid record value: reg = ${reg}")
            null
        }
    }

    private fun isValidRecord(reg: List<String>): Boolean {
        if(reg.size > 9) {
            log().warn("There are more info in the current record. Please add new columns. Record: {}",reg)
        }
        return reg.size >= 8 && reg.isNotEmpty() && reg[0].isNotBlank() && reg[1].isNotBlank()
    }

    private fun orElse0(text: String): Long {
        return if(text.isNotBlank()) { text.toLong() } else 0
    }

    private fun orElse(text: String, default: Long): Long {
        return if(text.isNotBlank()) { text.toLong() } else default
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
                        val pcrPositive: Long = 0,
                        val testAcPositive: Long = 0
)
