package com.bluevod.android.data.core.utils.mappers

interface NullableListMapper<in I, out O> {
    fun map(input: List<I?>?): List<O>
}