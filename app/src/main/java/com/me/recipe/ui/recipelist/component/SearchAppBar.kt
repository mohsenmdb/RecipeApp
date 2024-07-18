@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.recipelist.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.getAllFoodCategories
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.recipelist.component.chip.FoodCategoryChip
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun SearchAppBar(
    query: String,
    selectedCategory: FoodCategory?,
    categoryScrollPosition: Pair<Int, Int>,
    onQueryChanged: (String) -> Unit,
    newSearch: () -> Unit,
    onSearchClearClicked: () -> Unit,
    onSelectedCategoryChanged: (String) -> Unit,
    onCategoryScrollPositionChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberLazyListState()
    LaunchedEffect(key1 = scrollState) {
        scrollState.scrollToItem(
            categoryScrollPosition.first,
            categoryScrollPosition.second,
        )
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
    ) {
        Column {
            SearchTextField(query, onQueryChanged, newSearch, onSearchClearClicked)
            FoodChipsRow(
                scrollState,
                selectedCategory,
                newSearch,
                onSelectedCategoryChanged,
                onCategoryScrollPositionChanged,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SearchTextField(
    query: String,
    onQueryChanged: (String) -> Unit,
    newSearch: () -> Unit,
    onSearchClearClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    TextField(
        value = query,
        onValueChange = { onQueryChanged(it) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .testTag("testTag_SearchTextField"),
        label = {
            Text(text = "Search")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                newSearch()
                focusManager.clearFocus()
            },
        ),
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                contentDescription = "search",
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.clickable { onSearchClearClicked() },
                )
            }
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
        ),
    )
}

@Composable
private fun FoodChipsRow(
    scrollState: LazyListState,
    selectedCategory: FoodCategory?,
    newSearch: () -> Unit,
    onSelectedCategoryChanged: (String) -> Unit,
    onCategoryScrollPositionChanged: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.testTag("testTag_FoodChipsRow"),
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
                        scrollState.firstVisibleItemScrollOffset,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun SearchAppBarPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutPreview {
            SearchAppBar(
                query = FoodCategory.CHICKEN.name,
                selectedCategory = FoodCategory.CHICKEN,
                categoryScrollPosition = Pair(0, 0),
                onQueryChanged = {},
                newSearch = {},
                onCategoryScrollPositionChanged = { _, _ -> },
                onSelectedCategoryChanged = {},
                onSearchClearClicked = {},
            )
        }
    }
}
