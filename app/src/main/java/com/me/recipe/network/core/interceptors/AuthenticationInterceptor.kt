package com.me.recipe.network.core.interceptors

import android.util.Log
import com.me.recipe.cache.datastore.UserDataStore
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

const val NO_AUTHENTICATION = "NO-AUTHENTICATION"
const val AUTHORIZATION_HEADER = "Authorization"
private const val USER_AGENT_HEADER = "HTTP_CUSTOMUSERAGENT"
private const val USER_AGENT_VALUE = "Android Native Client"

class AuthenticationInterceptor @Inject constructor(
    private val userDataStore: UserDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        var request = request()
        val hasToken = request.header(NO_AUTHENTICATION)

        if (hasToken != "true") {
            val token = userDataStore.accessToken.value
            Timber.d("Need to add auth [%s]", token)
            request = if (token.isNotEmpty()) {
                request.newBuilder()
                    .addHeader(AUTHORIZATION_HEADER, "$token")
                    .addHeader(USER_AGENT_HEADER, USER_AGENT_VALUE)
                    .build()
            } else {
                request.newBuilder()
                    .addHeader(USER_AGENT_HEADER, USER_AGENT_VALUE)
                    .build()
            }
        }
        return@run proceed(request)
    }
}
