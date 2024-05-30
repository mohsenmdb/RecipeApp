package com.me.recipe.ui.recipe

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import com.me.recipe.ui.component.util.SharedTransitionLayoutPreview
import com.me.recipe.ui.utils.RobotTestRule
import javax.inject.Inject
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
    fun assertRecipeImageIsDisplayed() {
        composeTestRule.onNodeWithTag("testTag_RecipeImage", useUnmergedTree = true)
            .assertIsDisplayed()
    }

    operator fun invoke(
        robotTestRule: RobotTestRule,
        function: context(RobotTestRule)
        RecipeScreenRobot.() -> Unit
    ) {
        function(robotTestRule, this@RecipeScreenRobot)
    }
}
