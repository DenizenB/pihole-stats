package xyz.podd.piholestats

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

import xyz.podd.piholestats.model.Device
import xyz.podd.piholestats.service.Coordinator

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CoordinatorTest {
    lateinit var coordinator: Coordinator

    @BeforeAll
    fun init() {
        val devices = listOf(
            Device("pi3", "https://192.168.1.251:8080/admin/", "<password>", false),
            Device("pi2", "https://192.168.1.250:8080/admin/", "<password>", false)
        )
        coordinator = Coordinator(devices)
    }

    @Test
    fun getSummary() = runBlocking {
        println(coordinator.getSummary())
    }

    @Test
    fun getTopLists() = runBlocking {
        println(coordinator.getTopLists())
    }
}