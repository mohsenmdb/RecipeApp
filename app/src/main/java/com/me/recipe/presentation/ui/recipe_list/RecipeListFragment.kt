package com.me.recipe.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.me.recipe.presentation.component.LoadingRecipeListShimmer
import com.me.recipe.presentation.component.util.SnackbarController
import com.me.recipe.ui.theme.RecipeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeListFragment : Fragment() {
    @Inject
    lateinit var application: BaseApplication
    private val viewModel: RecipeListViewModel by viewModels()
    private val snackbarController = SnackbarController(lifecycleScope)

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
        val page = viewModel.page.value
        val scaffoldState = rememberScaffoldState()

        LaunchedEffect(key1 = viewModel.showSnackbar) {
            viewModel.showSnackbar.observe(viewLifecycleOwner) {
                it?.let {
                    snackbarController.getScope().launch {
                        snackbarController.showSnackbar(
                            scaffoldState = scaffoldState,
                            message = it,
                            actionLabel = getString(R.string.hide)
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
                    newSearch = { viewModel.onTriggerEvent(RecipeListEvent.NewSearchEvent) },
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
                if (isLoading && recipes.isEmpty())
                    LoadingRecipeListShimmer(250.dp)
                else
                    LazyColumn {
                        itemsIndexed(recipes) { index, recipe ->
                            viewModel.onChangeRecipeScrollPosition(index)
                            if ((index + 1) >= (page * PAGE_SIZE) && !isLoading)
                                viewModel.onTriggerEvent(RecipeListEvent.NextPageEvent)

                            RecipeCard(recipe = recipe, onClick = {
                                recipe.id?.let {
                                    val bundle = Bundle()
                                    bundle.putInt("recipeId", it)
                                    findNavController().navigate(
                                        R.id.action_recipeList_to_recipePage,
                                        bundle
                                    )
                                } ?:  snackbarController.getScope().launch {
                                        snackbarController.showSnackbar(
                                            scaffoldState = scaffoldState,
                                            message = getString(R.string.recipe_error),
                                            actionLabel = getString(R.string.hide)
                                        )
                                    }
                            })
                        }
                    }

                CircularIndeterminateProgressBar(isVisible = (isLoading && recipes.isNotEmpty()))

                DefaultSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
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
