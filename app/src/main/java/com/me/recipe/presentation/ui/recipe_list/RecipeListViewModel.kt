package com.me.recipe.presentation.ui.recipe_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.recipe.domain.model.Recipe
import com.me.recipe.presentation.component.FoodCategory
import com.me.recipe.presentation.component.getFoodCategory
import com.me.recipe.repository.RecipeRepository
import com.me.recipe.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

const val PAGE_SIZE = 30

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    @Named("auth_token") val apiToken: String,
    private val repository: RecipeRepository
) : ViewModel() {

    val recipes: MutableState<List<Recipe>> = mutableStateOf(listOf())
    val selectedCategory: MutableState<FoodCategory?> = mutableStateOf(null)
    val query = mutableStateOf("")
    val isLoading = mutableStateOf(false)
    var categoryScrollPosition: Pair<Int, Int> = 0 to 0
    val page = mutableStateOf(1)
    var recipeListScrollPosition = 0

    private val _showSnackbar : MutableLiveData<String?> = MutableLiveData()
    val showSnackbar: LiveData<String?>
        get() = _showSnackbar

    init {
        newSearch()
    }

    fun newSearch() = viewModelScope.launch {
        try {
            isLoading.value = true
            resetSearchState()
            delay(500)
            recipes.value = repository.search(apiToken, 1, query.value)
            isLoading.value = false
        } catch (e: Exception) {
            _showSnackbar.value = e.message
            isLoading.value = false
        }
    }
    fun nextPage()= viewModelScope.launch{
        // prevent duplicate event due to recompose happening to quickly
        if((recipeListScrollPosition + 1) >= (page.value * PAGE_SIZE) ){
            isLoading.value = true
            incrementPage()
            if(page.value > 1){
                val result = repository.search(token = apiToken, page = page.value, query = query.value )
                appendRecipes(result)
            }
            isLoading.value = false
        }
    }

    private fun appendRecipes(recipes: List<Recipe>) {
        val currentList = ArrayList(this.recipes.value)
        currentList.addAll(recipes)
        this.recipes.value = currentList
    }
    private fun incrementPage() {
        page.value = page.value + 1
    }

    fun onChangeRecipeScrollPosition(position: Int) {
        recipeListScrollPosition = position
    }

    private fun resetSearchState() {
        recipes.value = listOf()
        page.value = 1
        onChangeRecipeScrollPosition(0)
        if (selectedCategory.value?.value != query.value)
            clearSelectedCategory()
    }

    private fun clearSelectedCategory() {
        selectedCategory.value = null
    }

    fun onQueryChanged(newQuery: String) {
        query.value = newQuery
    }

    fun onSelectedCategoryChanged(category: String) {
        selectedCategory.value = getFoodCategory(category)
        onQueryChanged(category)
    }

    fun onCategoryScrollPositionChanged(position: Int, offset: Int) {
        categoryScrollPosition = position to offset
    }
}