package com.me.recipe.ui.recipelist

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollToIndex
import com.me.recipe.ui.component.util.FoodCategory
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.component.util.getAllFoodCategories
import com.me.recipe.ui.utils.RobotTestRule
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalSharedTransitionApi::class)
class RecipeListScreenRobot @Inject constructor() {

    context (RobotTestRule)
    fun setRecipeListScreen(
        state: RecipeListContract.State,
    ) {
        composeTestRule.setContent {
            SharedTransitionLayoutPreview {
                RecipeListScreen(
                    event = {},
                    effect = flowOf(),
                    state = state,
                    sharedTransitionScope = this,
                    animatedVisibilityScope = it,
                    navigateToRecipePage = { _, _, _ -> },
                )
            }
        }
    }

    context (RobotTestRule)
    fun setRecipeListScreenLoadingThenLoaded(
        data: RecipeListContract.State,
    ) {
        composeTestRule.setContent {
            var state by remember {
                mutableStateOf(data)
            }
            SharedTransitionLayoutPreview {
                RecipeListScreen(
                    event = {},
                    effect = flowOf(),
                    state = state,
                    sharedTransitionScope = this,
                    animatedVisibilityScope = it,
                    navigateToRecipePage = { _, _, _ -> },
                )
            }
            LaunchedEffect(Unit) {
                delay(1000)
                state = data.copy(loading = false)
            }
        }
    }

    context (RobotTestRule)
    fun checkScreenWhenStateIsLoaded(state: RecipeListContract.State) {
        assertSearchTextFieldIsDisplayed()
        assertMoreButtonIsDisplayed()
        assertFoodChipsRowIsDisplayed()
        val category = getAllFoodCategories()
        assertFirstFoodCategoryChipIsDisplayed(category)
        foodChipsRowScrollToIndex(category.lastIndex)
        assertLastFoodCategoryChipIsDisplayed(category)
    }

    context (RobotTestRule)
    private fun assertSearchTextFieldIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_SearchTextField", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertMoreButtonIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_MoreButton", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertFoodChipsRowIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_FoodChipsRow", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun foodChipsRowScrollToIndex(index: Int) {
        composeTestRule.onNodeWithTag("testTag_FoodChipsRow", useUnmergedTree = true)
            .performScrollToIndex(index)
    }

    context (RobotTestRule)
    private fun assertLastFoodCategoryChipIsDisplayed(category: List<FoodCategory>) {
        composeTestRule.onNodeWithText(category.last().value, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertFirstFoodCategoryChipIsDisplayed(category: List<FoodCategory>) {
        composeTestRule.onNodeWithText(category.first().value, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun mainClockAutoAdvance(
        autoAdvance: Boolean,
    ) {
        composeTestRule.mainClock.autoAdvance = autoAdvance
    }

    context (RobotTestRule)
    fun mainClockAdvanceTimeBy(
        milliseconds: Long,
        ignoreFrameDuration: Boolean = false,
    ) {
        composeTestRule.mainClock.advanceTimeBy(milliseconds, ignoreFrameDuration)
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule)
        RecipeListScreenRobot.() -> Unit,
    ) {
        function(robotTestRule, this@RecipeListScreenRobot)
    }
}
