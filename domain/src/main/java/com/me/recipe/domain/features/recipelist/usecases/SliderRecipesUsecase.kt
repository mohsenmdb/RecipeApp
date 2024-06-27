package com.me.recipe.domain.features.recipelist.usecases

import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.repository.RecipeListRepository
import com.me.recipe.shared.data.DataState
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

class SliderRecipesUsecase @Inject constructor(
    private val recipeListRepository: RecipeListRepository,
) {
    suspend operator fun invoke(): Flow<DataState<ImmutableList<Recipe>>> {
        return recipeListRepository.slider()
    }
}
