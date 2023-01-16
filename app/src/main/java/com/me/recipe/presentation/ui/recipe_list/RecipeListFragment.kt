package com.me.recipe.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.me.recipe.R
import com.me.recipe.presentation.component.RecipeCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    private val viewModel: RecipeListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PageContent()
            }
        }
    }

    @Composable
    fun PageContent() {
        val recipes = viewModel.recipes.value
        val query = viewModel.query.value
        val selectedCategory = viewModel.selectedCategory.value
        val isLoading = viewModel.isLoading.value
        Column {
            SearchAppBar(
                query = query,
                selectedCategory = selectedCategory,
                categoryScrollPosition = viewModel.categoryScrollPosition,
                onQueryChanged = viewModel::onQueryChanged,
                newSearch = viewModel::newSearch,
                onSelectedCategoryChanged = viewModel::onSelectedCategoryChanged,
                onCategoryScrollPositionChanged = viewModel::onCategoryScrollPositionChanged
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                LazyColumn {
                    itemsIndexed(recipes) { _, recipe ->
                        RecipeCard(recipe = recipe, onClick = {
                            findNavController().navigate(R.id.action_recipeListFragment_to_recipePageFragment)
                        })
                    }
                }
                CircularIndeterminateProgressBar(isLoading)
            }
        }
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        PageContent()
    }
}
