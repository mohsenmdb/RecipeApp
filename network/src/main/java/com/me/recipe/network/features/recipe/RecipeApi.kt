package com.me.recipe.network.features.recipe

import com.me.recipe.network.features.recipe.model.RecipeDto
import com.me.recipe.network.features.recipe.model.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {

    @GET("search")
    suspend fun search(
        @Query("page") page: Int,
        @Query("query") query: String,
    ): RecipeSearchResponse

    @GET("get")
    suspend fun get(
        @Query("id") id: Int,
    ): RecipeDto
}
