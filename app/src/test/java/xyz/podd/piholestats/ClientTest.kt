package xyz.podd.piholestats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xyz.podd.piholestats.model.network.Client

class ClientTest {
    @Test
    fun parcel() {
        val clients = arrayOf(
            Client("phone"),
            Client("phone|"),
            Client("|127.0.0.1"),
            Client("phone|127.0.0.1")
        )

        clients.forEach { originalClient ->
            val parcel = MockParcel.obtain()

            originalClient.writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            val parceledClient = Client.CREATOR.createFromParcel(parcel)

            assertEquals(originalClient.name, parceledClient.name)
            assertEquals(originalClient.address, parceledClient.address)
        }
    }
}