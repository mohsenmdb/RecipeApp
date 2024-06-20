package com.me.recipe.network.features.recipe.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RecipeSearchResponse(
    @Json(name = "count") var count: Int,
    @Json(name = "results") var results: List<RecipeDto>,
)
