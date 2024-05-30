package com.me.recipe.ui.utils

import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class HiltInjectRule(private val rule: HiltAndroidRule) : TestWatcher() {
    override fun starting(description: Description) {
        super.starting(description)
        rule.inject()
    }
}
