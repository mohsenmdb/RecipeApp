package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.shared.data.DataState
import com.me.recipe.shared.utils.RECIPE_PAGINATION_FIRST_PAGE
import com.me.recipe.shared.utils.RECIPE_PAGINATION_PAGE_SIZE
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class SearchRecipesUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) {
    suspend operator fun invoke(
        page: Int = RECIPE_PAGINATION_FIRST_PAGE,
        query: String = "",
        size: Int = RECIPE_PAGINATION_PAGE_SIZE,
    ): Flow<DataState<ImmutableList<Recipe>>> {
        return recipeListRepository.search(page, query, size)
    }
}
