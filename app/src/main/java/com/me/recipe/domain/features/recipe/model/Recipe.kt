package com.me.recipe.domain.features.recipe.model

import java.util.Date

data class Recipe(
    val id: Int,
    val title: String,
    val publisher: String,
    val featuredImage: String,
    val rating: Int,
    val sourceUrl: String,
    val ingredients: List<String>,
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
            ingredients = listOf(),
            dateAdded = Date(),
            dateUpdated = Date(),
        )
    }
}