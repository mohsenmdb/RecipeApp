package com.me.recipe.presentation.ui.recipe_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.me.recipe.presentation.ui.recipe.FoodCategoryChip
import kotlinx.coroutines.launch

@Composable
fun SearchAppBar(
    query: String,
    selectedCategory: FoodCategory?,
    categoryScrollPosition: Pair<Int, Int>,
    onQueryChanged: (String) -> Unit,
    newSearch: () -> Unit,
    onSelectedCategoryChanged: (String) -> Unit,
    onCategoryScrollPositionChanged: (Int, Int) -> Unit,
) {
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
                    onValueChange = { onQueryChanged(it) },
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
                            newSearch()
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
            val scope = rememberCoroutineScope()
            val scrollState = rememberLazyListState()
            LazyRow(
                state = scrollState,
                modifier = Modifier.padding(8.dp),
            ) {
                scope.launch {
                    scrollState.scrollToItem(
                        categoryScrollPosition.first,
                        categoryScrollPosition.second
                    )
                }
                items(getAllFoodCategories()) { category ->
                    FoodCategoryChip(
                        category = category.value,
                        isSelected = selectedCategory == category,
                        onClick = { newSearch() },
                        onSelectedCategoryChanged = {
                            onSelectedCategoryChanged(it)
                            onCategoryScrollPositionChanged(
                                scrollState.firstVisibleItemIndex,
                                scrollState.firstVisibleItemScrollOffset
                            )
                        },
                    )
                }
            }
        }
    }
}