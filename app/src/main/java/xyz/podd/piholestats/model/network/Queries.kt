package xyz.podd.piholestats.model.network

import kotlinx.serialization.Serializable

@Serializable
data class Queries(
	val data: List<QueryData>
) {
	val clients: List<Pair<Client, Int>>  by lazy {
		data
			.groupingBy { Client(it.client) }
			.eachCount()
			.toList()
			.sortedByDescending { it.second }
	}

	val domains: List<Pair<String, Int>> by lazy {
		data
			.groupingBy { it.domain }
			.eachCount()
			.toList()
			.sortedByDescending { it.second }
	}
}
