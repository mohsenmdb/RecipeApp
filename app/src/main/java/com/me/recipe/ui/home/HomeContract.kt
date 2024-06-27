package com.me.recipe.ui.home

import androidx.compose.runtime.Stable
import com.me.recipe.domain.features.recipe.model.CategoryRecipe
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.util.compose.UnidirectionalViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface HomeContract :
    UnidirectionalViewModel<HomeContract.Event, HomeContract.Effect, HomeContract.State> {

    sealed interface Event {
        data class ClickOnRecipeEvent(val recipe: Recipe) : Event
        data class LongClickOnRecipeEvent(val title: String) : Event
        data class OnChangeRecipeScrollPosition(val index: Int) : Event
        data object ToggleDarkTheme : Event
    }

    sealed interface Effect {
        data class ShowSnackbar(val message: String) : Effect
        data class NavigateToRecipePage(val recipe: Recipe) : Effect
    }

    @Stable
    data class State(
        val sliderRecipes: ImmutableList<Recipe> = persistentListOf(),
        val categoriesRecipes: ImmutableList<CategoryRecipe> = persistentListOf(),
        val errors: GenericDialogInfo? = null,
        val loading: Boolean = false,
        var recipeListScrollPosition: Int = 0,
    ) {
        companion object {
            fun testData() = State(
                sliderRecipes = persistentListOf(Recipe.testData()),
            )
        }
    }
}

val HomeContract.State.showShimmer: Boolean
    get() = loading && sliderRecipes.isEmpty()
