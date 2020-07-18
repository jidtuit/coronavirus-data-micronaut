package org.jid.coviddata.isciiicurrent

object CovidConstants {

    // In minutes: Once per hour
    const val REFRESH_MINUTES_COVID_DATA: Long = 60
    const val COVID_DATA_URL = "https://cnecovid.isciii.es/covid19/resources/datos_ccaas.csv"
    const val COVID_DATA_ENCODING = "ISO-8859-1"
    const val COVID_DAYS_OF_DATA: Long = 50
    const val COVID_TIMEZONE = "Europe/Madrid"

    const val CD_TOTAL_COLS = 7

    const val CD_CCAA_INDEX = 0
    const val CD_DATE_INDEX = 1
    const val CD_TOTAL_CASES_INDEX = 2
    const val CD_PCR_POSITIVE_INDEX = 3
    const val CD_TEST_AC_POSITIVE_INDEX = 4
    const val CD_TEST_OTHER_INDEX = 5
    const val CD_TEST_UNKNOWN_INDEX = 6

}