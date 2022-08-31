package com.me.recipe.network.responses

import com.google.gson.annotations.SerializedName
import com.me.recipe.network.model.RecipeNetworkEntity

data class RecipeSearchResponse(
    @SerializedName("count")
    var count: Int,
    @SerializedName("results")
    var results: List<RecipeNetworkEntity>
)