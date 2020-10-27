package xyz.podd.piholestats.model.network

import kotlinx.serialization.Serializable

@Serializable
data class Queries(
	val data: List<QueryData>
) {
	fun clients(): List<Pair<Client, Int>> =
		data
			.groupingBy { Client(it.client) }
			.eachCount()
			.toList()
			.sortedByDescending { it.second }
}
