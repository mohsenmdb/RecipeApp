package com.me.recipe.presentation.ui.recipe_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.me.recipe.R
import com.me.recipe.presentation.component.RecipeCard
import com.me.recipe.presentation.ui.recipe.FoodCategoryChip
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

        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                elevation = 4.dp,
            ) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        val focusManager = LocalFocusManager.current
                        TextField(
                            value = query,
                            onValueChange = { viewModel.onQueryChanged(it) },
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .padding(8.dp)
                                .background(MaterialTheme.colors.surface),
                            label = {
                                Text(text = "Search")
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    viewModel.newSearch()
                                    focusManager.clearFocus()
                                }
                            ),
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Search,
                                    contentDescription = "search"
                                )
                            },
                            textStyle = TextStyle(
                                color = MaterialTheme.colors.onSurface,
                            ),
                        )
                    }

                    LazyRow(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        itemsIndexed(getAllFoodCategories()) { _, category ->
                            FoodCategoryChip(
                                category = category.value,
                                isSelected = selectedCategory == category,
                                onClick = viewModel::newSearch,
                                onSelectedCategoryChanged = {
                                    viewModel.onSelectedCategoryChanged(it)
                                },
                            )
                        }
                    }
                }
            }
            LazyColumn {
                itemsIndexed(recipes) { _, recipe ->
                    RecipeCard(recipe = recipe, onClick = {
                        findNavController().navigate(R.id.action_recipeListFragment_to_recipePageFragment)
                    })
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
