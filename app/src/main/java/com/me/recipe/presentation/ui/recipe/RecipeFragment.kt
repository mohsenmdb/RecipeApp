package com.me.recipe.presentation.ui.recipe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.me.recipe.R
import com.me.recipe.presentation.BaseApplication
import com.me.recipe.presentation.component.CircularIndeterminateProgressBar
import com.me.recipe.presentation.component.DefaultSnackbar
import com.me.recipe.presentation.component.RecipeView
import com.me.recipe.presentation.component.util.LoadingRecipeListShimmer
import com.me.recipe.presentation.component.util.SnackbarController
import com.me.recipe.presentation.ui.recipe_list.RecipeListViewModel
import com.me.recipe.ui.theme.RecipeTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    @Inject
    lateinit var application: BaseApplication
    private val viewModel: RecipeViewModel by viewModels()
    private val snackbarController = SnackbarController(lifecycleScope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt("recipeId")?.let {
            viewModel.onTriggerEvent(RecipeEvent.GetRecipeEvent(it))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        val recipe = viewModel.recipe.value
        val isLoading = viewModel.isLoading.value
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
            scaffoldState = scaffoldState,
            snackbarHost = { scaffoldState.snackbarHostState }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colors.background)
            ) {
                if (isLoading && recipe == null)
                    /*TODO: add shimmer*/
                else
                    recipe?.let {
                        RecipeView(recipe = recipe)
                    }

                CircularIndeterminateProgressBar(isVisible = (isLoading && recipe == null))

                DefaultSnackbar(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    modifier = Modifier.align(Alignment.BottomCenter)
                ) {
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        RecipeTheme {
            PageContent()
        }
    }
}
