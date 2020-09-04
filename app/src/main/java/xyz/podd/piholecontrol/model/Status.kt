package xyz.podd.piholecontrol.model

import kotlinx.serialization.Serializable

@Serializable
data class Status(val status: String) {
	val enabled
		get() = status == "enabled"
}