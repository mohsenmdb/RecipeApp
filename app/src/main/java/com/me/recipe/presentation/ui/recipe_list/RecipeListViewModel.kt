package com.me.recipe.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.recipe.domain.model.Recipe
import com.me.recipe.repository.RecipeRepository
import com.me.recipe.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.FieldPosition
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    @Named("auth_token")  val apiToken: String,
    private val repository: RecipeRepository
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)
    val query = mutableStateOf("")
    var categoryScrollPosition :Float = 0f

    init {
        newSearch()
    }

    fun newSearch() = viewModelScope.launch {
        try {
            Log.d(TAG, "------------: ${query.value}")
            recipes.value = repository.search(apiToken, 1, query.value)
        } catch (e:Exception) {
            Log.d(TAG, "newSearch: ${e.message}")
        }
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery
    }

    fun onSelectedCategoryChanged(category: String) {
        selectedCategory.value = getFoodCategory(category)
        onQueryChanged(category)
    }

    fun onCategoryScrollPositionChanged(position: Float) {
        categoryScrollPosition = position
    }
}