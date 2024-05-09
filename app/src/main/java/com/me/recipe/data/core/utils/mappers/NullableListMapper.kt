package com.me.recipe.data.core.utils.mappers

interface NullableListMapper<in I, out O> {
    fun map(input: List<I?>?): List<O>
}
