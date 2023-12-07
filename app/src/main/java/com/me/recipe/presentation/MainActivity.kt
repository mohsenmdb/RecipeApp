package com.me.recipe.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.fragment.app.FragmentActivity
import com.me.recipe.cache.datastore.SettingsDataStore
import com.me.recipe.cache.datastore.UserDataStore
import com.me.recipe.presentation.ui.RecipeApp
import com.me.recipe.ui.theme.RecipeTheme
import com.me.recipe.util.getPushNotificationPermission
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    @Inject
    lateinit var settingsDataStore: SettingsDataStore

    @Inject
    lateinit var userDataStore: UserDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userDataStore.setAccessToken("Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
        getPushNotificationPermission()
        setContent {
            RecipeTheme(settingsDataStore.isDark.value) {
                RecipeApp()
            }
        }
    }
}
