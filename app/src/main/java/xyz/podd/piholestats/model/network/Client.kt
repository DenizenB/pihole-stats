package xyz.podd.piholestats.model.network

import kotlinx.serialization.Serializable

@Serializable
class Client(val name: String, val address: String) {
    constructor(client: String): this(
        client.substringBefore("|"),
        client.substringAfter("|")
    )

    override fun toString() = when (name.isEmpty()) {
        true -> address
        else -> name
    }

    override fun equals(other: Any?) = other is Client && address == other.address

    override fun hashCode() = address.hashCode()
}