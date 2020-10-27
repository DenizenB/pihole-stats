package xyz.podd.piholestats.service

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import xyz.podd.piholestats.model.*
import xyz.podd.piholestats.model.network.Summary
import xyz.podd.piholestats.model.network.TopLists

@OptIn(ExperimentalCoroutinesApi::class)
class Coordinator(private val devices: Collection<Device>) {
    suspend fun getSummary(): Summary =
        devices.asFlow()
            .map { it.service.getSummary() }
            .reduce { a, b -> a + b }

    suspend fun getTopLists(): TopLists =
        devices.asFlow()
            .map { it.service.getTopLists() }
            .reduce { a, b -> a + b }
}