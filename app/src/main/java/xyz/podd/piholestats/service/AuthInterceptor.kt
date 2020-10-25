package xyz.podd.piholestats.service

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authToken: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url().newBuilder()
            .addQueryParameter("auth", authToken)
            .build()

        val authenticatedRequest = request.newBuilder()
            .url(url)
            .build()

        return chain.proceed(authenticatedRequest)
    }
}