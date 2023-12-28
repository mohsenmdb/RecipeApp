@file:OptIn(ExperimentalFoundationApi::class)

package com.me.recipe.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.presentation.component.image.CoilImage

@Composable
fun RecipeCard(recipe: Recipe, onClick: () -> Unit, onLongClick: () -> Unit) {

    Card(
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(bottom = 6.dp, top = 6.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            CoilImage(
                data = recipe.featuredImage,
                contentDescription = "recipe image",
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(225.dp),
                contentScale = ContentScale.Crop
            )

            recipe.title.let { title ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)
                ) {
                    Text(
                        text = title,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .wrapContentWidth(Alignment.Start),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = recipe.rating.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }
}