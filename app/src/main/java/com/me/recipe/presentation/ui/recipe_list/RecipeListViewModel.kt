package com.me.recipe.presentation.ui.recipe_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(val string: String) : ViewModel() {

    var name = "mohsen"

}