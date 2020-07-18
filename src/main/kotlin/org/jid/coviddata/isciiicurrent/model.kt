package org.jid.coviddata.isciiicurrent

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZonedDateTime

data class CovidData(val area: String,
                     val dataDate: LocalDate,
                     val totalCases:Long = 0,
                     val totalCasesInc:Long = 0,
                     val pcrPositive: Long = 0,
                     val pcrPositiveInc: Long = 0,
                     val testAcPositive: Long = 0,
                     val testAcPositiveInc: Long = 0,
                     val otherTests: Long = 0,
                     val otherTestsInc: Long = 0,
                     val unknownSourcePositive: Long = 0,
                     val unknownSourcePositiveInc: Long = 0
)

data class CovidMetadata(val dataUrl: String, val notes: Flow<String>, val dataFrom: ZonedDateTime)

enum class Autonomy(val iso:String, name: String) {
    ANDALUCIA("AN", "ANDALUCÍA"),
    ARAGON("AR", "ARAGÓN"),
    ASTURIAS("AS", "ASTURIAS"),
    BALEARES("IB", "ISLAS BALEARES"),
    CANARIAS("CN", "CANARIAS"),
    CANTABRIA("CB", "CANTABRIA"),
    CYM("CM", "CASTILLA LA MANCHA"),
    CYL("CL", "CASTILLA Y LEÓN"),
    CATALUÑA("CT", "CATALUÑA"),
    PAISVASCO("PV", "PAÍS VASCO"),
    EXTREMADURA("EX", "EXTREMADURA"),
    GALICIA("GA", "GALICIA"),
    LARIOJA("RI", "LA RIOJA"),
    MADRID("MD", "MADRID"),
    MURCIA("MC", "MURCIA"),
    NAVARRA("NC", "NAVARRA"),
    VALENCIA("VC", "VALENCIA"),
    MELILLA("ML", "MELILLA"),
    CEUTA("CE", "CEUTA")
}


