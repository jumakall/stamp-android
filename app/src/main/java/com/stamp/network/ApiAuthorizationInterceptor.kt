package com.stamp.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiAuthorizationInterceptor : Interceptor {

    companion object {
        @Volatile
        private var INSTANCE: ApiAuthorizationInterceptor? = null
        fun getInstance() =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: ApiAuthorizationInterceptor().also {
                    INSTANCE = it
                }
            }

    }

    private var apiToken: String? = null

    fun setApiToken(token: String?) {
        apiToken = token
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()

        if (apiToken?.isNotBlank() == true)
            builder.addHeader("Authorization", "Bearer $apiToken")

        return chain.proceed(builder.build())
    }

}