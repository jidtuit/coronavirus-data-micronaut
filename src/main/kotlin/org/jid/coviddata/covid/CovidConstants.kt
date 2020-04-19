package org.jid.coviddata.covid

import io.micronaut.http.MediaType
import java.nio.charset.StandardCharsets

object CovidConstants {

    // In minutes: Once per hour
    const val REFRESH_MINUTES_COVID_DATA: Long = 60
    const val COVID_DATA_URL = "https://covid19.isciii.es/resources/serie_historica_acumulados.csv"
    const val COVID_DATA_ENCODING = "ISO-8859-1"
    const val COVID_DAYS_OF_DATA: Long = 50

}