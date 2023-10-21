package com.me.recipe.cache.datastore

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDataStore @Inject constructor(val context: Context){

  private val scope = CoroutineScope(Main)

  init {
    observeDataStore()
  }

  val accessToken = mutableStateOf("")

  fun setAccessToken(accessToken: String){
    scope.launch {
      context.dataStore.edit { preferences ->
        preferences[ACCESS_TOKEN_KEY] = accessToken
      }
    }
  }

  private fun observeDataStore(){
    context.dataStore.data.onEach { preferences ->
      preferences[ACCESS_TOKEN_KEY]?.let { token ->
        accessToken.value = token
      }
    }.launchIn(scope)
  }

  companion object{
    private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token_key")

    private const val APP_PREFERENCE_NAME = "user_info"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
      name = APP_PREFERENCE_NAME
    )
  }
}