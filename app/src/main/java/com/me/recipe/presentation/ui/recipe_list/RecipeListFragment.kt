package com.me.recipe.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.me.recipe.R
import com.me.recipe.presentation.BaseApplication
import com.me.recipe.presentation.component.RecipeCard
import com.me.recipe.presentation.component.SearchAppBar
import com.me.recipe.presentation.component.util.LoadingRecipeListShimmer
import com.me.recipe.ui.theme.RecipeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeListFragment : Fragment() {
    @Inject
    lateinit var application: BaseApplication
    private val viewModel: RecipeListViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                RecipeTheme(application.isDarkTheme.value) {
                    PageContent()
                }
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
                onCategoryScrollPositionChanged = viewModel::onCategoryScrollPositionChanged,
                onToggleTheme = {
                    application.changeDarkTheme()
                }
            )

            Box(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colors.background)) {
                if (isLoading)
                    LoadingRecipeListShimmer(250.dp)
                else
                    LazyColumn {
                        itemsIndexed(recipes) { _, recipe ->
                            RecipeCard(recipe = recipe, onClick = {
                                findNavController().navigate(R.id.action_recipeListFragment_to_recipePageFragment)
                            })
                        }
                    }
            }
        }
    }

    @Preview(showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        PageContent()
    }
}
