package com.me.recipe.ui.search

import androidx.compose.runtime.Stable
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.util.compose.UnidirectionalViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface SearchContract :
    UnidirectionalViewModel<SearchContract.Event, SearchContract.Effect, SearchContract.State> {

    sealed interface Event {
        //        data class OnPhoneNumberChanged(val phoneNumber: String) : Event
        data object NewSearchEvent : Event
        data object SearchClearEvent : Event
        data class OnQueryChanged(val query: String) : Event
        data class OnSelectedCategoryChanged(val category: String, val position: Int = 0, val offset: Int = 0) : Event
        data object RestoreStateEvent : Event
        data class OnRecipeLongClick(val title: String) : Event
        data class OnRecipeClick(val recipe: Recipe) : Event
        data class OnChangeRecipeScrollPosition(val index: Int) : Event
    }

    sealed interface Effect {
        data class ShowSnackbar(val message: String) : Effect
        data class NavigateToRecipePage(val recipe: Recipe) : Effect
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

val SearchContract.State.showShimmer: Boolean
    get() = loading && recipes.isEmpty()
val SearchContract.State.showLoadingProgressBar: Boolean
    get() = loading && recipes.isNotEmpty()
