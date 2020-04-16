package org.jid.coviddata.covid

object CovidConstants {

    // In minutes: Once per hour
    val REFRESH_MINUTES_COVID_DATA: Long = 60
    val COVID_DATA_URL = "https://covid19.isciii.es/resources/serie_historica_acumulados.csv"
    val COVID_DAYS_OF_DATA: Long = 50
}