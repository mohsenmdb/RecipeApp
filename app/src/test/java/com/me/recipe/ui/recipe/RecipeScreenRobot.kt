package com.me.recipe.ui.recipe

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.utils.RobotTestRule
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalSharedTransitionApi::class)
class RecipeScreenRobot @Inject constructor() {

    context (RobotTestRule)
    fun setRecipeScreen(state: RecipeContract.State) {
        composeTestRule.setContent {
            SharedTransitionLayoutPreview {
                RecipeScreen(
                    event = {},
                    effect = flowOf(),
                    state = state,
                    sharedTransitionScope = this,
                    animatedVisibilityScope = it,
                )
            }
        }
    }

    context (RobotTestRule)
    fun setRecipeScreenLoadingThenLoaded(data: RecipeContract.State) {
        composeTestRule.setContent {
            var state by remember {
                mutableStateOf(data)
            }
            SharedTransitionLayoutPreview {
                RecipeScreen(
                    event = {},
                    effect = flowOf(),
                    state = state,
                    sharedTransitionScope = this,
                    animatedVisibilityScope = it,
                )
            }
            LaunchedEffect(Unit) {
                delay(1000)
                state = data.copy(loading = false)
            }
        }
    }

    context (RobotTestRule)
    fun checkScreenWhenStateIsLoaded(ingredients: ImmutableList<String>) {
        assertRecipeImageIsDisplayed()
        assertTitleRowTextIsDisplayed()
        assertTitleRowIsDisplayed()
        assertRankChipIsDisplayed()
        assertRecipeInfoViewIsDisplayed()
        assertRecipeInfoViewTextIsDisplayed()
        assertRecipeInfoViewHorizontalDividerIsDisplayed()
        ingredients.forEach {
            assertIngredientIsDisplayed(it)
        }
        assertLoadingRankChipIsNotDisplayed()
        assertLoadingRecipeShimmerIsNotDisplayed()
    }

    context (RobotTestRule)
    fun checkScreenWhenStateIsLoading() {
        assertLoadingRankChipIsDisplayed()
        assertLoadingRecipeShimmerIsDisplayed()
        assertRecipeImageIsDisplayed()
        assertTitleRowTextIsDisplayed()
        assertTitleRowIsDisplayed()

        assertRankChipIsNotDisplayed()
        assertRecipeInfoViewIsNotDisplayed()
        assertRecipeInfoViewTextIsNotDisplayed()
        assertRecipeInfoViewHorizontalDividerIsNotDisplayed()
        assertIngredientTextIsNotDisplayed()
    }

    context (RobotTestRule)
    fun assertRecipeImageIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RecipeImage", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertTitleRowTextIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_TitleRow_Text", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertTitleRowIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_TitleRow", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertLoadingRankChipIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_LoadingRankChip", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertLoadingRankChipIsNotDisplayed() {
        composeTestRule.onNodeWithTag("testTag_LoadingRankChip", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context (RobotTestRule)
    fun assertRankChipIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RankChip", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertRankChipIsNotDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RankChip", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context (RobotTestRule)
    fun assertRecipeInfoViewIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RecipeInfoView", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertRecipeInfoViewIsNotDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RecipeInfoView", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context (RobotTestRule)
    fun assertRecipeInfoViewTextIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RecipeInfoView_Text", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertRecipeInfoViewTextIsNotDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RecipeInfoView_Text", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context (RobotTestRule)
    fun assertRecipeInfoViewHorizontalDividerIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RecipeInfoView_HorizontalDivider", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertRecipeInfoViewHorizontalDividerIsNotDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RecipeInfoView_HorizontalDivider", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context (RobotTestRule)
    fun assertLoadingRecipeShimmerIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_LoadingRecipeShimmer", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun assertLoadingRecipeShimmerIsNotDisplayed() {
        composeTestRule.onNodeWithTag("testTag_LoadingRecipeShimmer", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context (RobotTestRule)
    fun assertIngredientTextIsNotDisplayed() {
        composeTestRule.onNodeWithTag("testTag_ingredient_Text", useUnmergedTree = true)
            .assertIsNotDisplayed()
    }

    context (RobotTestRule)
    fun assertIngredientIsDisplayed(text: String) {
        composeTestRule.onNodeWithText(text, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    fun mainClockAutoAdvance(autoAdvance: Boolean) {
        composeTestRule.mainClock.autoAdvance = autoAdvance
    }

    context (RobotTestRule)
    fun mainClockAdvanceTimeBy(milliseconds: Long, ignoreFrameDuration: Boolean = false) {
        composeTestRule.mainClock.advanceTimeBy(milliseconds, ignoreFrameDuration)
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule)
        RecipeScreenRobot.() -> Unit
    ) {
        function(robotTestRule, this@RecipeScreenRobot)
    }
}
