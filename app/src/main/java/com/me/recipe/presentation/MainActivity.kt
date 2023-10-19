package com.me.recipe.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.me.recipe.R
import com.me.recipe.cache.datastore.SettingsDataStore
import com.me.recipe.presentation.ui.RecipeApp
import com.me.recipe.ui.theme.RecipeTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeTheme(settingsDataStore.isDark.value) {
                RecipeApp()
            }
        }
    }
}
