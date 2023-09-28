package com.bluevod.android.data.core.utils.mappers

interface NullableInputMapper<Input, Output> {
    fun map(input: Input?): Output
}