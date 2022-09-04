package com.me.recipe.repository

import com.me.recipe.domain.model.Recipe
import com.me.recipe.network.RecipeService
import com.me.recipe.network.model.RecipeDtoMapper

class RecipeRepositoryImpl(
    val recipeService: RecipeService,
    val mapper: RecipeDtoMapper
) : RecipeRepository {
    override suspend fun search(token: String, page: Int, query: String): List<Recipe> {
        return mapper.toDomainList(recipeService.search(token,page,query).results)
    }

    override suspend fun get(token: String, id: Int): Recipe {
        return mapper.mapToDomainModel(recipeService.get(token,id))
    }
}