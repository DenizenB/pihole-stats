package xyz.podd.piholecontrol.model

import android.icu.text.SimpleDateFormat
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import java.util.*

val timeFormat = SimpleDateFormat("HH:mm:ss")

@Serializable(with = QueryDataSerializer::class)
data class QueryData(
    val time: Long,
    val domain: String,
    val blocked: Boolean
) {
    val timeString: String
        get() = timeFormat.format(Date(time))
}

class QueryDataSerializer : KSerializer<QueryData> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Query", PrimitiveKind.STRING) // TODO look into descriptors

    override fun deserialize(decoder: Decoder): QueryData {
        check(decoder is JsonDecoder)

        val array = decoder.decodeJsonElement().jsonArray

        val time = array[0].jsonPrimitive.content.toLong()
        val domain = array[2].jsonPrimitive.content
        val blocked = when (array[4].jsonPrimitive.content.toInt()) {
            in 2..3 -> false // 2 = forwarded, 3 = cached
            else -> true
        }

        return QueryData(time, domain, blocked)
    }

    override fun serialize(encoder: Encoder, value: QueryData) = throw NotImplementedError()
}