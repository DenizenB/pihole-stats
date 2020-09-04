package xyz.podd.piholecontrol.service

import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import xyz.podd.piholecontrol.model.Status

interface PiHoleService {
	@GET("api.php?status")
	suspend fun getStatus(): Response<Status>
}