package com.me.recipe.presentation.ui.recipe_list

import androidx.lifecycle.ViewModel
import com.me.recipe.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    @Named("auth_token")  val apiToken: String,
    private val repository: RecipeRepository
) : ViewModel() {

    init {
        println("!!!!!!!--> $apiToken")
    }

}