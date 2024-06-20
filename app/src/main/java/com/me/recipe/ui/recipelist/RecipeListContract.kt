package com.me.recipe.ui.recipelist

import androidx.compose.runtime.Stable
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.util.FoodCategory
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.util.compose.UnidirectionalViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface RecipeListContract :
    UnidirectionalViewModel<RecipeListContract.Event, RecipeListContract.Effect, RecipeListContract.State> {

    sealed interface Event {
        //        data class OnPhoneNumberChanged(val phoneNumber: String) : Event
        data object NewSearchEvent : Event
        data class OnQueryChanged(val query: String) : Event
        data class OnSelectedCategoryChanged(val category: String) : Event
        data class OnCategoryScrollPositionChanged(val position: Int, val offset: Int) : Event
        data object RestoreStateEvent : Event
        data object ToggleDarkTheme : Event
        data class LongClickOnRecipeEvent(val title: String) : Event
        data class OnChangeRecipeScrollPosition(val index: Int) : Event
    }

    sealed interface Effect {
        data class ShowSnackbar(val message: String) : Effect
    }

    @Stable
    data class State(
        val recipes: ImmutableList<Recipe> = persistentListOf(),
        val errors: GenericDialogInfo? = null,
        val query: String = "",
        val selectedCategory: FoodCategory? = null,
        val loading: Boolean = false,
        // Pagination starts at '1' (-1 = exhausted)
        val page: Int = 1,
        var recipeListScrollPosition: Int = 0,
        var categoryScrollPosition: Pair<Int, Int> = 0 to 0,
    ) {
        companion object {
            fun testData() = State(
                recipes = persistentListOf(Recipe.testData()),
                query = FoodCategory.CHICKEN.name,
                selectedCategory = FoodCategory.CHICKEN,
            )
        }
    }
}

val RecipeListContract.State.showShimmer: Boolean
    get() = loading && recipes.isEmpty()
val RecipeListContract.State.showLoadingProgressBar: Boolean
    get() = loading && recipes.isNotEmpty()
