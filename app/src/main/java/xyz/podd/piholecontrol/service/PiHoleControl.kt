package xyz.podd.piholecontrol.service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager

class PiHoleControl {
	fun buildService(url: String): PiHoleService {
		val jsonConverter = Json.asConverterFactory(MediaType.get("application/json"))

		val retrofit = Retrofit.Builder()
			.baseUrl(url)
			.client(unsafeClient())
			.addConverterFactory(jsonConverter)
			.build()

		return retrofit.create(PiHoleService::class.java)
	}

	// https://stackoverflow.com/questions/37686625/disable-ssl-certificate-check-in-retrofit-library
	fun unsafeClient(): OkHttpClient {
		val trustAllCerts: X509TrustManager = object : X509TrustManager {
			override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

			override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

			override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
		}

		val sslContext = SSLContext.getInstance("SSL")
		sslContext.init(null, arrayOf(trustAllCerts), SecureRandom())

		val trustAllHostnames = HostnameVerifier { _, _ -> true }

		return OkHttpClient.Builder()
			.sslSocketFactory(sslContext.socketFactory, trustAllCerts)
			.hostnameVerifier(trustAllHostnames)
			.build()
	}
}