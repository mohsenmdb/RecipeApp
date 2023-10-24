package com.me.recipe.presentation.component

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SearchAppBar(
    query: String,
    selectedCategory: FoodCategory?,
    categoryScrollPosition: Pair<Int, Int>,
    onQueryChanged: (String) -> Unit,
    newSearch: () -> Unit,
    onSelectedCategoryChanged: (String) -> Unit,
    onCategoryScrollPositionChanged: (Int, Int) -> Unit,
    onToggleTheme: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
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
                        .background(MaterialTheme.colorScheme.surface),
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
                        color = MaterialTheme.colorScheme.onSurface,
                    ),
                )
                IconButton(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    onClick = { onToggleTheme() }) {
                    Icon(Icons.Filled.MoreVert, "")
                }
            }
            val scrollState = rememberLazyListState()
            LaunchedEffect(key1 = scrollState) {
                scrollState.scrollToItem(
                    categoryScrollPosition.first,
                    categoryScrollPosition.second
                )
            }
            LazyRow(
                state = scrollState,
                modifier = Modifier.padding(8.dp),
            ) {
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