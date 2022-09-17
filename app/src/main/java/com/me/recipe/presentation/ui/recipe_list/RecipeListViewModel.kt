package com.me.recipe.presentation.ui.recipe_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.recipe.domain.model.Recipe
import com.me.recipe.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    @Named("auth_token")  val apiToken: String,
    private val repository: RecipeRepository
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val query = mutableStateOf("")
    init {
        newSearch("chicken")
    }

    fun newSearch(query: String) = viewModelScope.launch {
        recipes.value = repository.search(apiToken, 1, query)
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery
    }
}