package xyz.podd.piholestats.model.network

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = TopListsSerializer::class)
data class TopLists(
    val queries: Map<String, Int>,
    val ads: Map<String, Int>,
    val clients: Map<Client, ClientStats>
) {
    operator fun plus(other: TopLists): TopLists = TopLists(
        mergeTopQueries(queries, other.queries),
        mergeTopQueries(ads, other.ads),
        mergeTopClients(clients, other.clients)
    )
}

class TopListsSerializer: KSerializer<TopLists> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("TopLists", PrimitiveKind.STRING) // TODO look into descriptors

    override fun deserialize(decoder: Decoder): TopLists {
        check(decoder is JsonDecoder)

        val json = decoder.decodeJsonElement().jsonObject

        val queries: Map<String, Int> = json["top_queries"]!!.jsonObject
            .mapValues { (_,v) -> v.jsonPrimitive.int }

        val ads: Map<String, Int> = json["top_ads"]!!.jsonObject
            .mapValues { (_,v) -> v.jsonPrimitive.int }

        val clients: Map<Client, ClientStats> = json["top_sources"]!!.jsonObject
            .mapKeys { (k,_) -> Client(k) }
            .mapValues { (_,v) -> ClientStats(queries = v.jsonPrimitive.int) }

        val clientsBlocked: Map<Client, ClientStats> = json["top_sources_blocked"]!!.jsonObject
            .mapKeys { (k,_) -> Client(k) }
            .mapValues { (_,v) -> ClientStats(blocked = v.jsonPrimitive.int) }

        return TopLists(queries, ads, mergeTopClients(clients, clientsBlocked))
    }

    override fun serialize(encoder: Encoder, value: TopLists) = throw NotImplementedError()
}

fun mergeTopQueries(a: Map<String, Int>, b: Map<String, Int>) =
    (a.asSequence() + b.asSequence())
        .groupingBy { it.key } // Group entries with the same key...
        .fold(0) { sum, entry -> sum + entry.value } // ... and calculate their sum
        .toList()
        .sortedBy { (_, value) -> value } // Sort by sum...
        .reversed() // ... descending
        .take(10)
        .toMap()

fun mergeTopClients(a: Map<Client, ClientStats>, b: Map<Client, ClientStats>): Map<Client, ClientStats> =
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