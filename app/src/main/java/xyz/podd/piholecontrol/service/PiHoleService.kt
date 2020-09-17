package xyz.podd.piholecontrol.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface PiHoleService {
	@GET("api.php?status")
	suspend fun getStatus(): Status

	@GET("api.php?summaryRaw")
	suspend fun getSummary(): Summary

	@GET("api.php?topItems")
	suspend fun getTopItems(@Query("auth") authToken: String): TopItems

	@POST("scripts/pi-hole/php/tailLog.php")
	suspend fun getTailLogHeight(@Body body: RequestBody): String
}

@Serializable
data class Status(val status: String) {
	val enabled
		get() = status == "enabled"
}

@Serializable
data class Summary(
	@SerialName("domains_being_blocked") val domainsInBlocklist: Int,
	@SerialName("dns_queries_today") val queriesToday: Int,
	@SerialName("ads_blocked_today") val blockedToday: Int,
	@SerialName("ads_percentage_today") val blockedTodayPercentage: Double,
	@SerialName("unique_domains") val uniqueDomains: Int,
	val status: String? = null
) {
	val enabled
		get() = status == "enabled"

	operator fun plus(other: Summary): Summary {
		val sumQueries = queriesToday + other.queriesToday
		val sumBlocked = blockedToday + other.blockedToday
		val blockedPercentage = sumBlocked.toDouble() / sumQueries

		return Summary(
			domainsInBlocklist + other.domainsInBlocklist,
			sumQueries,
			sumBlocked,
			blockedPercentage,
			uniqueDomains + other.uniqueDomains
		)
	}
}

@Serializable
data class TopItems(
	@SerialName("top_queries") val queries: Map<String, Int>,
	@SerialName("top_ads") val ads: Map<String, Int>
) {
	operator fun plus(other: TopItems): TopItems = TopItems(
		merge(queries, other.queries),
		merge(ads, other.ads)
	)

	private fun merge(a: Map<String, Int>, b: Map<String, Int>): Map<String, Int> {
		return (a.asSequence() + b.asSequence())
			.groupBy({ it.key }, { it.value }) // Join entries describing the same domain...
			.mapValues { (_, counts) -> counts.sum() } // ... and calculate their sum
			.toList()
			.sortedBy { (_, value) -> value } // Sort by count
			.reversed()
			.take(10)
			.toMap()
	}
}