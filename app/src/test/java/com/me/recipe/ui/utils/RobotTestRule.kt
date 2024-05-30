package com.me.recipe.ui.utils

import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.rules.RuleChain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
class RobotTestRule(
    private val testInstance: Any
) : TestRule {

    val composeTestRule = createComposeRule()
    override fun apply(base: Statement, description: Description): Statement {
        return RuleChain
            .outerRule(HiltAndroidAutoInjectRule(testInstance))
            .around(composeTestRule)
            .apply(base, description)
    }
}
