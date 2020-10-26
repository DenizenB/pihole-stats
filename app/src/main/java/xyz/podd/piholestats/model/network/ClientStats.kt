package xyz.podd.piholestats.model.network

import kotlinx.serialization.Serializable

@Serializable
data class ClientStats(var queries: Int = 0, var blocked: Int = 0) {
    operator fun plus(other: ClientStats): ClientStats {
        return ClientStats(
            queries + other.queries,
            blocked + other.blocked
        )
    }
}