package com.me.recipe.presentation.ui.recipe_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.me.recipe.R
import com.me.recipe.presentation.component.RecipeCard
import com.me.recipe.ui.theme.RecipeTheme
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

        Column {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colors.primary,
                elevation = 4.dp
            ) {
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
                                viewModel.newSearch(query)
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
            }
            LazyColumn {
                itemsIndexed(recipes) { index, recipe ->
                    RecipeCard(recipe = recipe, onClick = {
                        findNavController().navigate(R.id.action_recipeListFragment_to_recipePageFragment)
                    })
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
