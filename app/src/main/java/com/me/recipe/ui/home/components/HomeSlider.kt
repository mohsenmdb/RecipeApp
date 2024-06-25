@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.me.recipe.ui.home.components

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.ui.home.HomeContract
import com.me.recipe.ui.navigation.HomeDestination
import com.me.recipe.ui.recipelist.component.RecipeCard
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeSlider(
    recipes: ImmutableList<Recipe>,
    event: (HomeContract.Event) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = recipes::size)

    Box {
        HorizontalPager(
            state = pagerState,
            modifier = modifier.padding(top = 50.dp, bottom = 32.dp),
            contentPadding = PaddingValues(horizontal = 8.dp),
            pageSpacing = 20.dp,
        ) { index ->
            val pagerOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
            val imageSize by animateFloatAsState(
                targetValue = if (pagerOffset != 0.0f) 1f else 1.05f,
                animationSpec = tween(durationMillis = 200),
                label = "",
            )
            RecipeCard(
                recipe = recipes[index],
                onClick = {
                    event.invoke(HomeContract.Event.ClickOnRecipeEvent(recipes[index]))
                },
                onLongClick = {
                    event.invoke(HomeContract.Event.LongClickOnRecipeEvent(recipes[index].title))
                },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                recipePageStarter = HomeDestination.route,
                modifier = Modifier.then(
                    if (pagerState.currentPage == index) {
                        Modifier
                            .fillMaxWidth()
                            .graphicsLayer {
                                scaleX = imageSize
                                scaleY = imageSize
                            }
                    } else {
                        Modifier.fillMaxWidth()
                    },
                ),
            )
        }
    }
}
