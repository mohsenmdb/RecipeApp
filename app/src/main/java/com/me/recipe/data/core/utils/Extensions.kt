package com.bluevod.android.data.core.utils

import androidx.core.text.HtmlCompat

fun String.fromHtml(): String {
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)
        .toString()
}
