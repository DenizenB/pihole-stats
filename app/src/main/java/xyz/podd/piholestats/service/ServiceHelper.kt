package xyz.podd.piholestats.service

import android.annotation.SuppressLint
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.*
import retrofit2.Retrofit
import xyz.podd.piholestats.model.Device
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSession
import javax.net.ssl.X509TrustManager

@ExperimentalSerializationApi
object ServiceHelper {
	private val client by lazy {
		OkHttpClient.Builder()
			.callTimeout(3, TimeUnit.SECONDS)
			.addInterceptor(LoggingInterceptor())
			.build()
	}
	private val jsonFactory by lazy { Json { ignoreUnknownKeys = true }.asConverterFactory(MediaType.get("application/json")) }

	fun buildService(device: Device): PiHoleService {
		val retrofit = Retrofit.Builder()
			.baseUrl(device.url)
			.addConverterFactory(jsonFactory)
			.client(buildClient(device))
			.build()

		return retrofit.create(PiHoleService::class.java)
	}

	// https://stackoverflow.com/questions/37686625/disable-ssl-certificate-check-in-retrofit-library
	private fun buildClient(device: Device): OkHttpClient {
		val builder = client.newBuilder()
			.cookieJar(SetCookieJar())
			.addInterceptor(AuthInterceptor(device.authToken))

		if (!device.verifySsl) {
			val trustManager = TrustAllCerts()

			val sslContext = SSLContext.getInstance("SSL")
			sslContext.init(null, arrayOf(trustManager), SecureRandom())

			builder
				.sslSocketFactory(sslContext.socketFactory, trustManager)
				.hostnameVerifier(TrustAllHostnames())
		}

		return builder.build()
	}
}

// If CookieJar is shared between multiple backends, then this implementation breaks.
private class SetCookieJar: CookieJar {
	private val _cookies: MutableSet<Cookie> = HashSet()

	override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
		_cookies.addAll(cookies)
	}

	override fun loadForRequest(url: HttpUrl): MutableList<Cookie> = _cookies.toMutableList()
}

@SuppressLint("TrustAllX509TrustManager")
private class TrustAllCerts: X509TrustManager {
	override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

	override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

	override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
}

@SuppressLint("BadHostnameVerifier")
private class TrustAllHostnames: HostnameVerifier {
	override fun verify(p0: String?, p1: SSLSession?): Boolean = true
}