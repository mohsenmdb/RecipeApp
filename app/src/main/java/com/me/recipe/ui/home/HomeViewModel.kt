package com.me.recipe.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.CategoriesRecipesUsecase
import com.me.recipe.domain.features.recipelist.usecases.SliderRecipesUsecase
import com.me.recipe.shared.datastore.SettingsDataStore
import com.me.recipe.shared.datastore.SettingsDataStore.Companion.DARK_THEME_KEY
import com.me.recipe.shared.utils.TAG
import com.me.recipe.shared.utils.getAllFoodCategories
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.PositiveAction
import com.me.recipe.ui.home.HomeContract.Event
import com.me.recipe.ui.home.HomeContract.Event.LongClickOnRecipeEvent
import com.me.recipe.ui.home.HomeContract.Event.ToggleDarkTheme
import com.me.recipe.util.errorformater.ErrorFormatter
import com.me.recipe.util.errorformater.exceptions.RecipeDataException
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sliderRecipesUsecase: Lazy<SliderRecipesUsecase>,
    private val categoriesRecipesUsecase: Lazy<CategoriesRecipesUsecase>,
    private val settingsDataStore: SettingsDataStore,
    private val errorFormatter: Lazy<ErrorFormatter>,
) : ViewModel(), HomeContract {

    private val _state = MutableStateFlow(HomeContract.State(sliderLoading = true, categoriesLoading = true))
    override val state: StateFlow<HomeContract.State> = _state.asStateFlow()

    private val effectChannel = Channel<HomeContract.Effect>(Channel.UNLIMITED)
    override val effect: Flow<HomeContract.Effect> = effectChannel.receiveAsFlow()

    override fun event(event: Event) {
        viewModelScope.launch {
            try {
                when (event) {
                    is ToggleDarkTheme -> toggleDarkTheme()
                    is Event.ClickOnRecipeEvent -> handleOnRecipeClicked(event.recipe)
                    is LongClickOnRecipeEvent ->
                        effectChannel.trySend(HomeContract.Effect.ShowSnackbar(event.title))
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e("Exception: %s", e)
                if (e.message != null) {
                    effectChannel.trySend(HomeContract.Effect.ShowSnackbar(e.message!!))
                }
            } finally {
                Timber.tag(TAG).d("launchJob: finally called.")
            }
        }
    }

    init {
        observeThemeState()

        viewModelScope.launch {
            fetchSliderRecipes()
            fetchCategoriesRecipes()
        }
    }

    private fun handleOnRecipeClicked(recipe: Recipe) {
        try {
            if (recipe.id == Recipe.EMPTY.id) throw RecipeDataException()
            effectChannel.trySend(HomeContract.Effect.NavigateToRecipePage(recipe))
        } catch (e: Exception) {
            effectChannel.trySend(HomeContract.Effect.ShowSnackbar(errorFormatter.get().format(e)))
        }
    }

    private suspend fun fetchSliderRecipes() {
        sliderRecipesUsecase.get().invoke()
            .onEach { dataState ->
                _state.update { it.copy(sliderLoading = dataState.loading) }
                dataState.data?.let { list ->
                    _state.update { it.copy(sliderRecipes = list) }
                }
                dataState.error?.let { error ->
                    showErrorDialog(errorFormatter.get().format(error))
                }
            }.launchIn(viewModelScope)
    }

    private suspend fun fetchCategoriesRecipes() {
        categoriesRecipesUsecase.get().invoke(getAllFoodCategories())
            .onEach { dataState ->
                _state.update { it.copy(categoriesLoading = dataState.loading) }
                dataState.data?.let { list ->
                    _state.update { it.copy(categoriesRecipes = list) }
                }
                dataState.error?.let { error ->
                    showErrorDialog(errorFormatter.get().format(error))
                }
            }.launchIn(viewModelScope)
    }

    private fun showErrorDialog(error: String) {
        val errors = GenericDialogInfo.Builder()
            .title(R.string.error)
            .description(error)
            .positive(
                PositiveAction(
                    positiveBtnTxt = R.string.ok,
                    onPositiveAction = {
                        _state.update { it.copy(errors = null) }
                    },
                ),
            )
            .onDismiss { _state.update { it.copy(errors = null) } }
            .build()
        _state.update { it.copy(errors = errors) }
    }

    private fun toggleDarkTheme() {
        settingsDataStore.toggleTheme()
    }

    private fun observeThemeState() {
        settingsDataStore.observeIsDark().onEach { isDark ->
            _state.update { it.copy(isDark = isDark[DARK_THEME_KEY] == true) }
        }.launchIn(viewModelScope)
    }
}
