package org.jid.coviddata.covid

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime

data class CovidData(val area: String,
                     val dataDate: LocalDate,
                     val totalCases:Long = 0,
                     val totalCasesInc:Long = 0,
                     val hospitalCases:Long = 0,
                     val hospitalCasesInc:Long = 0,
                     val uciCases:Long = 0,
                     val uciCasesInc:Long = 0,
                     val deathCases:Long = 0,
                     val deathCasesInc:Long = 0,
                     val recoveredCases: Long = 0,
                     val recoveredCasesInc: Long = 0
)

data class CovidMetadata(val dataUrl: String, val notes: Flow<String>, val dataFrom: LocalDateTime)

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


