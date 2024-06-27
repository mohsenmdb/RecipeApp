package com.me.recipe.util.errorformater

interface ErrorFormatter {
    fun format(throwable: Throwable?): String
}
