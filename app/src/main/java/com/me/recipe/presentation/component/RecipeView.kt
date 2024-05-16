@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.presentation.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.presentation.component.image.CoilImage
import java.util.Date
import kotlinx.collections.immutable.ImmutableList

@Composable
fun RecipeView(
    recipe: Recipe,
    isLoading: Boolean,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        with(sharedTransitionScope) {
            RecipeImage(
                id = recipe.id,
                image = recipe.featuredImage,
                animatedVisibilityScope = animatedVisibilityScope,
            )
            RecipeInfoColumn(
                recipe = recipe,
                animatedVisibilityScope = animatedVisibilityScope,
                isLoading = isLoading,
            )
        }
    }
}

@Composable
private fun SharedTransitionScope.RecipeImage(
    id: Int,
    image: String,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    CoilImage(
        data = image,
        contentDescription = "Recipe Featured Image",
        modifier = Modifier
            .sharedBounds(
                rememberSharedContentState(key = "image-$id"),
                animatedVisibilityScope = animatedVisibilityScope,
            )
            .fillMaxWidth()
            .height(250.dp),
        contentScale = ContentScale.Crop,
    )
}

@Composable
private fun SharedTransitionScope.RecipeInfoColumn(
    recipe: Recipe,
    animatedVisibilityScope: AnimatedVisibilityScope,
    isLoading: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        TitleRow(
            id = recipe.id,
            title = recipe.title,
            rank = recipe.rating.toString(),
            animatedVisibilityScope = animatedVisibilityScope,
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
        RankChip(rank)
    }
}

@Composable
private fun RankChip(rank: String) {
    AssistChip(
        shape = CircleShape,
        onClick = { },
        label = {
            Text(
                text = rank,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        leadingIcon = {
            Icon(
                Icons.Filled.ThumbUp,
                contentDescription = "ThumbUp Icon",
                Modifier.size(AssistChipDefaults.IconSize),
                tint = Color.Red,
            )
        },
    )
}

@Composable
private fun RecipeInfoView(
    dateUpdated: Date,
    publisher: String,
    ingredients: ImmutableList<String>,
) {
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
