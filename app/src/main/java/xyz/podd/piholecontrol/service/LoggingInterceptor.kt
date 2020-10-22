package xyz.podd.piholecontrol.service

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

const val TAG = "PiHoleService"

class LoggingInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d(TAG, "Requesting ${request.url()}")

        return chain.proceed(request)
    }
}