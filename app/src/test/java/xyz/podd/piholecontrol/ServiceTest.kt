package xyz.podd.piholecontrol

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import xyz.podd.piholecontrol.model.Device

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ServiceTest {
    lateinit var device: Device

    @BeforeAll
    fun init() {
        device = Device("pi3", "https://192.168.1.251:8080/admin/", "<password>", false)
    }

    @Test
    fun getStatus() = runBlocking {
        println(device.service.getStatus())
    }

    @Test
    fun getSummary() = runBlocking {
        println(device.service.getSummary())
    }

    @Test
    fun getTopItems() = runBlocking {
        println(device.service.getTopItems(device.authToken))
    }

    @Test
    fun getTopClients() = runBlocking {
        println(device.service.getTopClients(device.authToken))
    }

    @Test
    fun getTopClientsBlocked() = runBlocking {
        println(device.service.getTopClientsBlocked(device.authToken))
    }
}