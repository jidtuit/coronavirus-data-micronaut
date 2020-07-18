package org.jid.coviddata.isciiicurrent

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.jid.coviddata.isciiicurrent.CovidConstants.CD_CCAA_INDEX
import org.jid.coviddata.isciiicurrent.CovidConstants.CD_DATE_INDEX
import org.jid.coviddata.isciiicurrent.CovidConstants.CD_PCR_POSITIVE_INDEX
import org.jid.coviddata.isciiicurrent.CovidConstants.CD_TEST_AC_POSITIVE_INDEX
import org.jid.coviddata.isciiicurrent.CovidConstants.CD_TEST_OTHER_INDEX
import org.jid.coviddata.isciiicurrent.CovidConstants.CD_TEST_UNKNOWN_INDEX
import org.jid.coviddata.isciiicurrent.CovidConstants.CD_TOTAL_CASES_INDEX
import org.jid.coviddata.isciiicurrent.CovidConstants.CD_TOTAL_COLS
import org.jid.coviddata.isciiicurrent.CovidConstants.COVID_DAYS_OF_DATA
import org.jid.coviddata.utils.logging.Loggable
import org.jid.coviddata.utils.logging.log
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE
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
                                item.pcrPositive + acc.pcrPositive,
                                item.pcrPositiveInc + acc.pcrPositiveInc,
                                item.testAcPositive + acc.testAcPositive,
                                item.testAcPositiveInc + acc.testAcPositiveInc,
                                item.otherTests + acc.otherTests,
                                item.otherTestsInc + acc.otherTestsInc,
                                item.unknownSourcePositive + acc.unknownSourcePositive,
                                item.unknownSourcePositiveInc + acc.unknownSourcePositiveInc
                        )
                    }
                }
    }


    private fun row2Data(today: CovidDataRow, dayBefore: CovidData? = null) : CovidData {
        return CovidData(today.area,
                today.dataDate,
                today.totalCases,
                today.totalCases - (dayBefore?.totalCases ?: 0),
                today.pcrPositive,
                today.pcrPositive - (dayBefore?.pcrPositive ?: 0),
                today.testAcPositive,
                today.testAcPositive - (dayBefore?.testAcPositive ?: 0),
                today.otherTests,
                today.otherTests - (dayBefore?.otherTests ?: 0),
                today.unknownTests,
                today.unknownTests - (dayBefore?.unknownSourcePositive ?: 0)

        )

    }

    private fun toCovidDataRow(line: String): CovidDataRow? {
        val reg = line.split(",")

        return if(isValidRecord(reg)) {
            CovidDataRow(reg[CD_CCAA_INDEX].trim(),
                    LocalDate.parse(reg[CD_DATE_INDEX], ISO_DATE),
                    totalOrElseSumPositives(reg),
                    orElse0(reg[CD_PCR_POSITIVE_INDEX]),
                    orElse0(reg[CD_TEST_AC_POSITIVE_INDEX]),
                    orElse0(reg[CD_TEST_OTHER_INDEX]),
                    orElse0(reg[CD_TEST_UNKNOWN_INDEX])
            )
        } else {
            log().warn("Invalid record value: reg = ${reg}")
            null
        }
    }

    private fun isValidRecord(reg: List<String>): Boolean {
        if(reg.size > CD_TOTAL_COLS) {
            log().warn("There are more info in the current record. Please add new columns. Record: {}",reg)
        }
        return reg.size >= CD_TOTAL_COLS && reg.isNotEmpty() && reg[0].isNotBlank() && reg[1].isNotBlank()
    }

    private fun totalOrElseSumPositives(reg: List<String>): Long {
        return orElse(reg[CD_TOTAL_CASES_INDEX],
                orElse0(reg[CD_PCR_POSITIVE_INDEX]) + orElse0(reg[CD_TEST_AC_POSITIVE_INDEX])
                + orElse0(reg[CD_TEST_OTHER_INDEX]) + orElse0(reg[CD_TEST_UNKNOWN_INDEX]))
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
                        val pcrPositive: Long = 0,
                        val testAcPositive: Long = 0,
                        val otherTests:Long = 0,
                        val unknownTests:Long = 0
)
