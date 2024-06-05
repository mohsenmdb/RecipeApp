package com.me.recipe.ui.recipelist

import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.utils.RobotTestRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class RecipeListScreenTest {

    @get:Rule
    val robotTestRule = RobotTestRule(this)

    @Inject
    lateinit var robot: RecipeListScreenRobot

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun `when all data is available then show recipe correctly`() {
        val state = RecipeListContract.State.testData()
        robot(robotTestRule) {
            setRecipeListScreen(state)
            checkScreenWhenStateIsLoaded(state)
        }
    }


    @Test
    fun `when state change from loading to loaded show correctly loading and loaded screens`() {
        val loadedState = RecipeListContract.State.testData()
        val loadingState = RecipeListContract.State.testData().copy(
            recipes = persistentListOf(),
            loading = true,
        )
        robot(robotTestRule) {
            setRecipeListScreenLoadingThenLoaded(loadingState, loadedState)

            checkScreenWhenStateIsLoading()
            mainClockAutoAdvance(false)
            mainClockAdvanceTimeBy(1100)
            checkScreenWhenStateIsLoaded(loadedState)
        }
    }


    @Test
    fun `while load more data show loading progress bar correctly`() {
        val state = RecipeListContract.State.testData().copy(loading = true)
        robot(robotTestRule) {
            setRecipeListScreen(state)
            checkScreenWhenStateIsLoadedMore()
        }
    }


    @Test
    fun `when has error check error dialog show correctly`() {
        val state = RecipeListContract.State.testData().copy(
            errors = GenericDialogInfo.testDate()
        )
        robot(robotTestRule) {
            setRecipeListScreen(state)
            checkScreenWhenStateIsError(state.errors!!)
        }
    }
}
