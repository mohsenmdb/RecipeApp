package com.me.recipe.presentation.ui.recipe_list

import androidx.compose.runtime.Stable
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.presentation.component.FoodCategory
import com.me.recipe.presentation.component.util.GenericDialogInfo
import com.me.recipe.util.compose.UnidirectionalViewModel

interface RecipeListContract :
    UnidirectionalViewModel<RecipeListContract.Event, RecipeListContract.Effect, RecipeListContract.State> {

    sealed interface Event {
        //        data class OnPhoneNumberChanged(val phoneNumber: String) : Event
        data object NewSearchEvent : Event
        data object NextPageEvent : Event
        data object RestoreStateEvent : Event
        data class LongClickOnRecipeEvent(val title: String) : Event
    }

    sealed interface Effect {
        data class ShowSnackbar(val message: String) : Effect
    }

    @Stable
    data class State(
        val recipes: List<Recipe> = listOf(),
        val errors: GenericDialogInfo? = null,
        val query: String = "",
        val selectedCategory: FoodCategory? = null,
        val loading: Boolean = false,
        // Pagination starts at '1' (-1 = exhausted)
        val page: Int = 1,
        var recipeListScrollPosition: Int = 0,
        var categoryScrollPosition: Pair<Int, Int> = 0 to 0
    )
}
