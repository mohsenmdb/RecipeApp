package com.me.recipe.presentation

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    //it should be stored in shared preferences
    val isDarkTheme = mutableStateOf(false)

    fun changeDarkTheme() {
        isDarkTheme.value= !isDarkTheme.value
    }
}