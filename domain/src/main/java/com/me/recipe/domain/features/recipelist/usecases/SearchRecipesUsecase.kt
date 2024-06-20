package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.core.data.DataState
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class SearchRecipesUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) {
    suspend operator fun invoke(page: Int, query: String): Flow<DataState<ImmutableList<Recipe>>> {
        return recipeListRepository.search(page, query)
    }
}
