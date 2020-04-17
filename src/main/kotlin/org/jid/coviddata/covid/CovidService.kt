package org.jid.coviddata.covid

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jid.coviddata.covid.CovidConstants.COVID_DATA_URL
import org.jid.coviddata.covid.CovidConstants.REFRESH_MINUTES_COVID_DATA
import java.net.URL
import java.time.LocalDateTime
import javax.inject.Singleton

@Singleton
class CovidService(private val parser: CovidParser) {

    private var cache: CovidServiceCache = runBlocking { getCache() }

    suspend fun getCovidDataByAutonomyAndCountry(): Flow<CovidData> {
        if(cache.isOutdated()) {
            cache = getCache()
        }
        return cache.data
    }

    private suspend fun getCache(): CovidServiceCache {
        val rawData = fetchData()
        val data = parser.parseData(rawData)
        return CovidServiceCache(data, LocalDateTime.now())
    }

    private suspend fun fetchData(): String = coroutineScope {
        withContext(Dispatchers.IO) {
            val readText = URL(COVID_DATA_URL).readText()
            //println("readText = ${readText}")
            readText
        }
    }
}

data class CovidServiceCache(val data: Flow<CovidData>, val date: LocalDateTime)
fun CovidServiceCache.isOutdated() = this.date.isBefore(LocalDateTime.now().minusMinutes(REFRESH_MINUTES_COVID_DATA))