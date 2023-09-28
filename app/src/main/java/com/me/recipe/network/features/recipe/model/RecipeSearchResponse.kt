package com.me.recipe.network.features.recipe.model

import com.google.gson.annotations.SerializedName

data class RecipeSearchResponse(
    @SerializedName("count")
    var count: Int,
    @SerializedName("results")
    var results: List<RecipeNetwork>
)