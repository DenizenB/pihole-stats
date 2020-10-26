package xyz.podd.piholestats.model.network

import kotlinx.serialization.Serializable

@Serializable
data class Queries(
	val data: List<QueryData>
)