package xyz.podd.piholecontrol.service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PiHoleService {
	@GET("api.php?status")
	suspend fun getStatus(): Status

	@GET("api.php?summary")
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
	val status: String,
	@SerialName("domains_being_blocked") val domainsInBlocklist: String,
	@SerialName("dns_queries_today") val queriesToday: String,
	@SerialName("ads_blocked_today") val blockedToday: String,
	@SerialName("ads_percentage_today") val blockedTodayPercentage: String,
	@SerialName("unique_domains") val uniqueDomains: String
) {
	val enabled
		get() = status == "enabled"
}

@Serializable
data class TopItems(
	@SerialName("top_queries") val queries: Map<String, Int>,
	@SerialName("top_ads") val ads: Map<String, Int>
)