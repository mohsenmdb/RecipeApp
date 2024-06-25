package com.me.recipe.util.errorformater

import android.content.Context
import com.me.recipe.BuildConfig
import com.me.recipe.R
import com.me.recipe.util.errorformater.exceptions.ReadableException
import com.me.recipe.util.errorformater.exceptions.RecipeDataException
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import javax.net.ssl.SSLHandshakeException

class ErrorFormatterImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : ErrorFormatter {
    override fun format(throwable: Throwable?): String {
        return formater(throwable)
    }

    private fun formater(throwable: Throwable?): String {
        val errorMessage: StringResource = when (throwable) {
            is ReadableException -> StringResource.Text(throwable.readableMessage)
            is RecipeDataException -> StringResource.ResId(R.string.something_went_wrong)
            is UnknownHostException -> StringResource.ResId(R.string.not_connected_to_internet)
            is SocketTimeoutException, is TimeoutException -> StringResource.ResId(R.string.connection_timeout_exception)
            is SSLHandshakeException -> StringResource.ResId(R.string.server_error_retry)
            else -> StringResource.ResId(R.string.server_error_retry)
        }

        if (BuildConfig.DEBUG) {
            return buildString {
                append(errorMessage.resolve(context))
                appendLine()
                appendLine()
                append("*****Debug Info*****")
                appendLine()
                append(throwable.toString())
                appendLine()
                append("**********")
            }
        }
        return errorMessage.resolve(context)
    }
}
