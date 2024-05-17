@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.presentation.ui.recipe.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.presentation.component.util.SharedTransitionLayoutTest
import com.me.recipe.presentation.ui.recipe.components.chip.LoadingRankChip
import com.me.recipe.presentation.ui.recipe.components.chip.RankChip
import com.me.recipe.presentation.ui.recipe.components.shimmer.LoadingRecipeShimmer
import com.me.recipe.ui.theme.RecipeTheme
import java.util.Date
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun SharedTransitionScope.RecipeContent(
    recipe: Recipe,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        TitleRow(
            id = recipe.id,
            title = recipe.title,
            rank = recipe.rating.toString(),
            animatedVisibilityScope = animatedVisibilityScope,
            isLoading = isLoading,
        )
        if (!isLoading) {
            RecipeInfoView(
                dateUpdated = recipe.dateUpdated,
                publisher = recipe.publisher,
                ingredients = recipe.ingredients,
            )
        } else {
            LoadingRecipeShimmer()
        }
    }
}

@Composable
private fun SharedTransitionScope.TitleRow(
    id: Int,
    title: String,
    rank: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isLoading: Boolean,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .sharedBounds(
                    rememberSharedContentState(key = "title-$id"),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
                .wrapContentWidth(Alignment.Start)
                .weight(1f),
        )
        if (isLoading) {
            LoadingRankChip()
        } else {
            RankChip(rank)
        }
    }
}

@Composable
private fun RecipeInfoView(
    dateUpdated: Date,
    publisher: String,
    ingredients: ImmutableList<String>,
) {
    Column {
        Text(
            text = stringResource(R.string.recipe_date_and_publisher, dateUpdated, publisher),
            modifier = Modifier
                .fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        HorizontalDivider(
            modifier = Modifier
                .padding(vertical = 8.dp),
        )
        for (ingredient in ingredients) {
            Text(
                text = ingredient,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Preview
@Composable
private fun RecipeContentPreview() {
    RecipeTheme(true) {
        SharedTransitionLayoutTest {
            RecipeContent(
                recipe = Recipe.testData(),
                animatedVisibilityScope = it,
                isLoading = false,
            )
        }
    }
}
