package com.me.recipe.domain.features.recipe_list.usecases

import com.me.recipe.data.core.utils.DataState
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipe_list.repository.RecipeListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRecipesUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository
) {
    suspend operator fun invoke(
        token: String,
        page: Int,
        query: String
    ): Flow<DataState<List<Recipe>>> {
        return recipeListRepository.search(token, page, query)
    }
}