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
import androidx.test.platform.app.InstrumentationRegistry
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.getAllFoodCategories
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
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
                    navigateToRecipePage = { _ -> },
                )
            }
        }
    }

    context (RobotTestRule)
    fun setRecipeListScreenLoadingThenLoaded(
        loadingState: RecipeListContract.State,
        loadedState: RecipeListContract.State,
    ) {
        composeTestRule.setContent {
            var state by remember {
                mutableStateOf(loadingState)
            }
            SharedTransitionLayoutPreview {
                RecipeListScreen(
                    event = {},
                    effect = flowOf(),
                    state = state,
                    sharedTransitionScope = this,
                    animatedVisibilityScope = it,
                    navigateToRecipePage = { _ -> },
                )
            }
            LaunchedEffect(Unit) {
                delay(1000)
                state = loadedState
            }
        }
    }

    context (RobotTestRule)
    fun checkScreenWhenStateIsLoading() {
        assertRecipeListShimmerIsDisplayed()
        assertShimmerRecipeCardItemIsDisplayed(0)
        recipeListShimmerScrollToIndex(1)
        assertShimmerRecipeCardItemIsDisplayed(1)
        recipeListShimmerScrollToIndex(2)
        assertShimmerRecipeCardItemIsDisplayed(2)
        recipeListShimmerScrollToIndex(3)
        assertShimmerRecipeCardItemIsDisplayed(3)
        recipeListShimmerScrollToIndex(4)
        assertShimmerRecipeCardItemIsDisplayed(4)
    }

    context (RobotTestRule)
    fun checkScreenWhenStateIsLoaded(
        state: RecipeListContract.State,
    ) {
        assertSearchTextFieldIsDisplayed()
        assertMoreButtonIsDisplayed()
        assertFoodChipsRowIsDisplayed()

        val category = getAllFoodCategories()
        assertFirstFoodCategoryChipIsDisplayed(category)
        foodChipsRowScrollToIndex(category.lastIndex)
        assertLastFoodCategoryChipIsDisplayed(category)

        assertFirstRecipeImageIsDisplayed(state.recipes.first())
        assertFirstRecipeTitleIsDisplayed(state.recipes.first())
        assertFirstRecipeRatingIsDisplayed(state.recipes.first())
        recipeListScrollToIndex(state.recipes.lastIndex)
        assertLastRecipeImageIsDisplayed(state.recipes.last())
        assertLastRecipeTitleIsDisplayed(state.recipes.last())
        assertLastRecipeRatingIsDisplayed(state.recipes.last())
    }

    context (RobotTestRule)
    fun checkScreenWhenStateIsLoadedMore() {
        assertLoadMoreProgressBarIsDisplay()
        assertLoadMoreTextIsDisplay()
    }

    context (RobotTestRule)
    fun checkScreenWhenStateIsError(
        errors: GenericDialogInfo,
    ) {
        val context = InstrumentationRegistry.getInstrumentation().context
        val positiveText = context.getString(errors.positiveAction?.positiveBtnTxt!!)
        assertGenericDialogIsDisplayed()
        assertGenericDialogDescriptionIsDisplayed(errors.description!!)
        assertGenericDialogPositiveButtonIsDisplayed(positiveText)
        assertGenericDialogNegativeButtonIsDisplayed(errors.negativeAction?.negativeBtnTxt!!)
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
    private fun recipeListScrollToIndex(index: Int) {
        composeTestRule.onNodeWithTag("testTag_RecipeList", useUnmergedTree = true)
            .performScrollToIndex(index)
    }

    context (RobotTestRule)
    private fun assertFirstRecipeImageIsDisplayed(recipe: Recipe) {
        composeTestRule.onNodeWithTag("testTag_RecipeCard_Image_${recipe.id}", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertFirstRecipeTitleIsDisplayed(recipe: Recipe) {
        composeTestRule.onNodeWithText(recipe.title, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertFirstRecipeRatingIsDisplayed(recipe: Recipe) {
        composeTestRule.onNodeWithText(recipe.rating.toString(), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertLastRecipeImageIsDisplayed(recipe: Recipe) {
        composeTestRule.onNodeWithTag("testTag_RecipeCard_Image_${recipe.id}", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertLastRecipeTitleIsDisplayed(recipe: Recipe) {
        composeTestRule.onNodeWithText(recipe.title, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertLastRecipeRatingIsDisplayed(recipe: Recipe) {
        composeTestRule.onNodeWithText(recipe.rating.toString(), useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertRecipeListShimmerIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RecipeListShimmer", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun recipeListShimmerScrollToIndex(index: Int) {
        composeTestRule.onNodeWithTag("testTag_RecipeListShimmer", useUnmergedTree = true)
            .performScrollToIndex(index)
    }

    context (RobotTestRule)
    private fun assertShimmerRecipeCardItemIsDisplayed(cardNumber: Int) {
        composeTestRule.onNodeWithTag("testTag_ShimmerRecipeCardItem_$cardNumber", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertLoadMoreProgressBarIsDisplay() {
        composeTestRule.onNodeWithTag("testTag_LoadingView_CircularProgressIndicator", useUnmergedTree = true)
            .assertIsDisplayed()
    }
    context (RobotTestRule)
    private fun assertLoadMoreTextIsDisplay() {
        composeTestRule.onNodeWithTag("testTag_LoadingView_Text", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertGenericDialogIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_GenericDialog", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertGenericDialogDescriptionIsDisplayed(description: String) {
        composeTestRule.onNodeWithText(description, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertGenericDialogPositiveButtonIsDisplayed(buttonText: String) {
        composeTestRule.onNodeWithText(buttonText, useUnmergedTree = true)
            .assertIsDisplayed()
    }

    context (RobotTestRule)
    private fun assertGenericDialogNegativeButtonIsDisplayed(buttonText: String) {
        composeTestRule.onNodeWithText(buttonText, useUnmergedTree = true)
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
