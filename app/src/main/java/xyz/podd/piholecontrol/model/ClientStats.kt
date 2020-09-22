package xyz.podd.piholecontrol.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.reflect.KMutableProperty1

@Serializable
data class ClientStats(var queries: Int = 0, var blocked: Int = 0) {
    operator fun plus(other: ClientStats): ClientStats {
        return ClientStats(
            queries + other.queries,
            blocked + other.blocked
        )
    }
}

abstract class ClientStatsSerializer(keySerializer: KSerializer<Client>, valueSerializer: KSerializer<ClientStats>): KSerializer<Map<Client, ClientStats>> {
    override val descriptor: SerialDescriptor = keySerializer.descriptor
    abstract val valueField: KMutableProperty1<ClientStats, Int>

    override fun deserialize(decoder: Decoder): Map<Client, ClientStats> {
        check(decoder is JsonDecoder)

        val json = decoder.decodeJsonElement().jsonObject
        val map = LinkedHashMap<Client, ClientStats>(json.size)
        json.asSequence().forEach {
            val client = Client(it.key)
            val stats = ClientStats()
            valueField.set(stats, it.value.jsonPrimitive.int)

            map[client] = stats
        }
        return map
    }

    override fun serialize(encoder: Encoder, value: Map<Client, ClientStats>) = throw NotImplementedError()
}

class ClientQueriesSerializer(keySerializer: KSerializer<Client>, valueSerializer: KSerializer<ClientStats>): ClientStatsSerializer(keySerializer, valueSerializer) {
    override val valueField = ClientStats::queries
}

class ClientBlockedSerializer(keySerializer: KSerializer<Client>, valueSerializer: KSerializer<ClientStats>): ClientStatsSerializer(keySerializer, valueSerializer) {
    override val valueField = ClientStats::blocked
}

fun mergeClientStats(a: Map<Client, ClientStats>, b: Map<Client, ClientStats>): Map<Client, ClientStats> =
    (a.asSequence() + b.asSequence())
        .groupingBy { it.key } // Group by client...
        .aggregate { _, acc: ClientStats?, (_, stats), _ ->
            acc?.plus(stats) ?: stats // ... and combine the results
        }
        .toList()
        .sortedBy { (_, value) -> value.queries } // Sort by queries...
        .reversed() // ... descending
        .take(10)
        .toMap()