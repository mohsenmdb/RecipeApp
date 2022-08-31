package com.me.recipe.domain.model

data class Recipe(
    val id: Int? = null,
    val cookingInstructions: String? = null,
    val dateAdded: String? = null,
    val dateUpdated: String? = null,
    val description: String? = null,
    val featuredImage: String? = null,
    val ingredients: List<String> = listOf(),
    val longDateAdded: Int? = null,
    val longDateUpdated: Int? = null,
    val publisher: String? = null,
    val rating: Int? = null,
    val sourceUrl: String? = null,
    val title: String? = null,
)