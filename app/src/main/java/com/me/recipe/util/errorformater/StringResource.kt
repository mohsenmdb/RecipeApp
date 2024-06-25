package com.me.recipe.util.errorformater

import android.content.Context
import androidx.annotation.StringRes

sealed interface StringResource {

    fun resolve(context: Context): String

    data class Text(val text: String) : StringResource {
        override fun resolve(context: Context): String {
            return text
        }
    }
    data class ResId(@StringRes val stringId: Int) : StringResource {
        override fun resolve(context: Context): String {
            return context.getString(stringId)
        }
    }
    data class ResIdWithParams(@StringRes val stringId: Int, val params: List<Any>) :
        StringResource {
        override fun resolve(context: Context): String {
            return context.getString(stringId, *params.toTypedArray())
        }
    }
}
