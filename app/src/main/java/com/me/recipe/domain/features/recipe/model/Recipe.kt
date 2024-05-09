package com.me.recipe.domain.features.recipe.model

import androidx.compose.runtime.Immutable
import java.util.Date
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class Recipe(
    val id: Int,
    val title: String,
    val publisher: String,
    val featuredImage: String,
    val rating: Int,
    val sourceUrl: String,
    val ingredients: ImmutableList<String>,
    val dateAdded: Date,
    val dateUpdated: Date,
) {
    companion object {
        val EMPTY = Recipe(
            id = -1,
            title = "",
            publisher = "",
            featuredImage = "",
            rating = 0,
            sourceUrl = "",
            ingredients = persistentListOf(),
            dateAdded = Date(),
            dateUpdated = Date(),
        )
    }
}
