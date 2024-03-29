package com.me.recipe.network.features.recipe

import com.me.recipe.network.features.recipe.model.RecipeNetwork
import com.me.recipe.network.features.recipe.model.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RecipeService {

    @GET("search")
    suspend fun search(
        @Query("page") page: Int,
        @Query("query") query: String
    ): RecipeSearchResponse

    @GET("get")
    suspend fun get(
        @Query("id") id: Int
    ): RecipeNetwork

}