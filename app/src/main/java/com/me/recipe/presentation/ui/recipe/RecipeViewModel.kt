package com.me.recipe.presentation.ui.recipe

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.recipe.domain.model.Recipe
import com.me.recipe.presentation.ui.recipe_list.RecipeListEvent
import com.me.recipe.repository.RecipeRepository
import com.me.recipe.util.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named


@HiltViewModel
class RecipeViewModel @Inject constructor(
    @Named("auth_token") private val apiToken: String,
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    val recipe: MutableState<Recipe?> = mutableStateOf(null)
    val isLoading = mutableStateOf(false)

    private val _showSnackbar: MutableLiveData<String?> = MutableLiveData()
    val showSnackbar: LiveData<String?>
        get() = _showSnackbar


    fun onTriggerEvent(event: RecipeEvent) = viewModelScope.launch {
        try {
            when (event) {
                is RecipeEvent.GetRecipeEvent -> {
                    getRecipe(event.id)
                }
            }
        } catch (e: Exception) {
            _showSnackbar.value = e.message
            isLoading.value = false
        } finally {
            Log.d(TAG, "launchJob: finally called.")
        }
    }


    private suspend fun getRecipe(id: Int){
        isLoading.value = true
        recipe.value = recipeRepository.get(apiToken, id)
        isLoading.value = false
    }


}