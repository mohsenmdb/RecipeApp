@file:OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.recipelist.component

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.component.image.CoilImage
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.theme.RecipeTheme

@Composable
internal fun RecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier
            .padding(bottom = 6.dp, top = 6.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            with(sharedTransitionScope) {
                CoilImage(
                    data = recipe.featuredImage,
                    contentDescription = "recipe image",
                    modifier = Modifier
                        .sharedBounds(
                            rememberSharedContentState(key = "image-${recipe.uid}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                        .fillMaxWidth()
                        .requiredHeight(225.dp)
                        .testTag("testTag_RecipeCard_Image_${recipe.id}"),
                    contentScale = ContentScale.Crop,
                )
                RecipeInfoRow(recipe, animatedVisibilityScope)
            }
        }
    }
}

@Composable
private fun SharedTransitionScope.RecipeInfoRow(
    recipe: Recipe,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 8.dp),
    ) {
        Text(
            text = recipe.title,
            modifier = Modifier
                .sharedBounds(
                    rememberSharedContentState(key = "title-${recipe.uid}"),
                    animatedVisibilityScope = animatedVisibilityScope,
                )
                .fillMaxWidth(0.8f)
                .wrapContentWidth(Alignment.Start),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = recipe.rating.toString(),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(
    group = "firstGroup",
    showBackground = true,
    backgroundColor = 0xFF292C3C,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun RecipeCardPreview() {
    RecipeTheme {
        SharedTransitionLayoutPreview {
            RecipeCard(
                recipe = com.me.recipe.domain.features.recipe.model.Recipe.testData(),
                onClick = {},
                onLongClick = {},
                sharedTransitionScope = this,
                animatedVisibilityScope = it,
            )
        }
    }
}
