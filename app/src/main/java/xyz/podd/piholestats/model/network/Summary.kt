package xyz.podd.piholestats.model.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
	@SerialName("domains_being_blocked")
	val domainsInBlocklist: Int,

	@SerialName("dns_queries_today")
	val queriesToday: Int,

	@SerialName("ads_blocked_today")
	val blockedToday: Int,

	@SerialName("ads_percentage_today")
	val blockedTodayPercentage: Double,

	@SerialName("unique_domains")
	val uniqueDomains: Int,

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