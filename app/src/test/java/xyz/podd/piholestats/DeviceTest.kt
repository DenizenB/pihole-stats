package xyz.podd.piholestats

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import xyz.podd.piholestats.model.Device

class DeviceTest {
    @Test
    fun parcel() {
        val devices = arrayOf(
            Device("pi3", "https://someaddress", "somePassword", true),
            Device("pi4", "http://someaddress", "somePass", false)
        )

        devices.forEach { originalDevice ->
            val parcel = MockParcel.obtain()

            originalDevice.writeToParcel(parcel, 0)
            parcel.setDataPosition(0)
            val parceledDevice = Device.CREATOR.createFromParcel(parcel)

            assertEquals(originalDevice.name, parceledDevice.name)
            assertEquals(originalDevice.url, parceledDevice.url)
            assertEquals(originalDevice.password, parceledDevice.password)
            assertEquals(originalDevice.verifySsl, parceledDevice.verifySsl)
        }
    }
}