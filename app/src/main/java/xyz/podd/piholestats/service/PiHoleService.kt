package xyz.podd.piholestats.service

import retrofit2.http.*
import xyz.podd.piholestats.model.network.Queries
import xyz.podd.piholestats.model.network.Status
import xyz.podd.piholestats.model.network.Summary
import xyz.podd.piholestats.model.network.TopLists

interface PiHoleService {
	@GET("api.php?status")
	suspend fun getStatus(): Status

	@GET("api.php?summaryRaw")
	suspend fun getSummary(): Summary

	@GET("api.php?topItems&topClients&topClientsBlocked")
	suspend fun getTopLists(): TopLists

	@GET("api.php?getAllQueries")
	suspend fun getQueries(): Queries

	@GET("api.php")
	suspend fun getQueries(@Query("getAllQueries") count: Int): Queries

	@GET("api.php?getAllQueries")
	suspend fun getQueriesByTime(@Query("from") from: Long, @Query("until") until: Long): Queries

	@GET("api.php?getAllQueries")
	suspend fun getQueriesByClient(@Query("client") client: String): Queries

	@GET("api.php?getAllQueries")
	suspend fun getQueriesByDomain(@Query("domain") domain: String): Queries
}