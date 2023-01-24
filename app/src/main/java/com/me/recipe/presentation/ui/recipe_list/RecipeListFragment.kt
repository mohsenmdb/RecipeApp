package com.me.recipe.presentation.ui.recipe_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.me.recipe.R
import com.me.recipe.presentation.BaseApplication
import com.me.recipe.presentation.component.*
import com.me.recipe.presentation.component.util.LoadingRecipeListShimmer
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        val scaffoldState = rememberScaffoldState()

        LaunchedEffect(key1 = viewModel.showSnackbar) {
            viewModel.showSnackbar.observe(viewLifecycleOwner) {
                it?.let {
                    lifecycleScope.launch{
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = it,
                            actionLabel = getString(R.string.hide),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            }
        }

        Scaffold(
            topBar = {
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
            },
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState }
//            bottomBar = { MyBottomNav() },
//            drawerContent = { MyDrawer() },
        ) { padding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colors.background)
            ) {
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

                DefaultSnackbar(snackbarHostState = scaffoldState.snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)) {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
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
