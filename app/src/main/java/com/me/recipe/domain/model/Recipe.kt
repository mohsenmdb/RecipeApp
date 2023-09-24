package com.me.recipe.domain.model

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
)