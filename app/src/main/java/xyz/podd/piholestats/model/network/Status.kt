package xyz.podd.piholestats.model.network

import kotlinx.serialization.Serializable

@Serializable
data class Status(val status: String) {
	val enabled
		get() = status == "enabled"
}