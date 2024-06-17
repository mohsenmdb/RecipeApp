package com.me.recipe.ui.recipe

import androidx.compose.runtime.Stable
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.util.compose.UnidirectionalViewModel

interface RecipeContract :
    UnidirectionalViewModel<RecipeContract.Event, RecipeContract.Effect, RecipeContract.State> {

    sealed interface Event {
        data class GetRecipeEvent(val id: Int) : Event
    }

    sealed interface Effect {
        data class ShowSnackbar(val message: String) : Effect
    }

    @Stable
    data class State(
        val recipe: com.me.recipe.domain.features.recipe.model.Recipe = com.me.recipe.domain.features.recipe.model.Recipe.EMPTY,
        val loading: Boolean = false,
    ) {
        companion object {
            fun testData() = State(
                recipe = com.me.recipe.domain.features.recipe.model.Recipe.testData(),
            )
        }
    }
}
