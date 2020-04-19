package org.jid.coviddata.covid

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.jid.coviddata.covid.CovidConstants.COVID_DATA_ENCODING
import org.jid.coviddata.covid.CovidConstants.COVID_DATA_URL
import org.jid.coviddata.covid.CovidConstants.REFRESH_MINUTES_COVID_DATA
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import javax.inject.Singleton

@Singleton
class CovidService(private val parser: CovidParser) {

    private var cache: CovidServiceCache = runBlocking { getCache() }

    suspend fun getCovidDataByAutonomyAndCountry(): Flow<CovidData> = getFromCache { it.data }

    suspend fun getCovidMetadataInfo(): CovidMetadata = getFromCache { it.metadata }

    private suspend fun <T> getFromCache(f: (CovidServiceCache) -> T ): T {
        if(cache.isOutdated()) {
            cache = getCache()
        }
        return f.invoke(cache)
    }

    private suspend fun getCache(): CovidServiceCache {
        val dataDate = LocalDateTime.now()
        val rawData = fetchData(COVID_DATA_URL, Charset.forName(COVID_DATA_ENCODING))

        val data = parser.parseData(rawData)
        val metadata = CovidMetadata(COVID_DATA_URL, parser.parseMetadataInfo(rawData), dataDate)

        return CovidServiceCache(data, metadata, dataDate)
    }

    private suspend fun fetchData(url: String, charset: Charset = StandardCharsets.UTF_8): String = coroutineScope {
        withContext(Dispatchers.IO) {
            val readText = URL(url).readText(charset)
            //println("readText = ${readText}")
            readText
        }
    }
}

data class CovidServiceCache(val data: Flow<CovidData>,
                             val metadata: CovidMetadata,
                             val date: LocalDateTime = LocalDateTime.now())
fun CovidServiceCache.isOutdated() = this.date.isBefore(LocalDateTime.now().minusMinutes(REFRESH_MINUTES_COVID_DATA))