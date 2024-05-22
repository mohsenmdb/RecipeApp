package com.me.recipe.presentation.ui.recipelist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.recipe.R
import com.me.recipe.cache.datastore.SettingsDataStore
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.RestoreRecipesUsecase
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase
import com.me.recipe.presentation.component.util.FoodCategory
import com.me.recipe.presentation.component.util.GenericDialogInfo
import com.me.recipe.presentation.component.util.PositiveAction
import com.me.recipe.presentation.component.util.getFoodCategory
import com.me.recipe.presentation.ui.recipelist.RecipeListContract.Event.LongClickOnRecipeEvent
import com.me.recipe.presentation.ui.recipelist.RecipeListContract.Event.NewSearchEvent
import com.me.recipe.presentation.ui.recipelist.RecipeListContract.Event.NextPageEvent
import com.me.recipe.presentation.ui.recipelist.RecipeListContract.Event.OnCategoryScrollPositionChanged
import com.me.recipe.presentation.ui.recipelist.RecipeListContract.Event.OnQueryChanged
import com.me.recipe.presentation.ui.recipelist.RecipeListContract.Event.OnSelectedCategoryChanged
import com.me.recipe.presentation.ui.recipelist.RecipeListContract.Event.RestoreStateEvent
import com.me.recipe.presentation.ui.recipelist.RecipeListContract.Event.ToggleDarkTheme
import com.me.recipe.util.TAG
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
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
class RecipeListViewModel @Inject constructor(
    private val searchRecipesUsecase: Lazy<SearchRecipesUsecase>,
    private val restoreRecipesUsecase: Lazy<RestoreRecipesUsecase>,
    private val savedStateHandle: SavedStateHandle,
    private val settingsDataStore: SettingsDataStore,
) : ViewModel(), RecipeListContract {

    private val _state = MutableStateFlow(RecipeListContract.State(loading = true))
    override val state: StateFlow<RecipeListContract.State> = _state.asStateFlow()

    private val effectChannel = Channel<RecipeListContract.Effect>(Channel.UNLIMITED)
    override val effect: Flow<RecipeListContract.Effect> = effectChannel.receiveAsFlow()

    override fun event(event: RecipeListContract.Event) {
        viewModelScope.launch {
            try {
                when (event) {
                    is NewSearchEvent -> newSearch()
                    is OnQueryChanged -> onQueryChanged(event.query)
                    is OnSelectedCategoryChanged -> onSelectedCategoryChanged(event.category)
                    is OnCategoryScrollPositionChanged -> onCategoryScrollPositionChanged(event.position, event.offset)
                    is ToggleDarkTheme -> toggleDarkTheme()
                    is NextPageEvent -> nextPage(event.index)
                    is RestoreStateEvent -> restoreState()
                    is LongClickOnRecipeEvent -> effectChannel.trySend(RecipeListContract.Effect.ShowSnackbar(event.title))
                    is RecipeListContract.Event.OnChangeRecipeScrollPosition -> onChangeRecipeScrollPosition(event.index)
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e("Exception: %s", e)
                _state.update { it.copy(loading = false) }
                if (e.message != null) {
                    effectChannel.trySend(RecipeListContract.Effect.ShowSnackbar(e.message!!))
                }
            } finally {
                Timber.tag(TAG).d("launchJob: finally called.")
            }
        }
    }

    init {
        savedStateHandle.get<Int>(STATE_KEY_PAGE)?.let { p ->
            setPage(p)
        }
        savedStateHandle.get<String>(STATE_KEY_QUERY)?.let { q ->
            setQuery(q)
        }
        savedStateHandle.get<Int>(STATE_KEY_LIST_POSITION)?.let { p ->
            setListScrollPosition(p)
        }
        savedStateHandle.get<FoodCategory>(STATE_KEY_SELECTED_CATEGORY)?.let { c ->
            setSelectedCategory(c)
        }

        if (state.value.recipeListScrollPosition != 0) {
            event(RestoreStateEvent)
        } else {
            event(NewSearchEvent)
        }
    }

    private suspend fun restoreState() {
        restoreRecipesUsecase.get().invoke(page = state.value.page, query = state.value.query)
            .onEach { dataState ->
                _state.update { it.copy(loading = dataState.loading) }

                dataState.data?.let { list ->
                    _state.update { it.copy(recipes = list) }
                }

                dataState.error?.let { error ->
//                dialogQueue.appendErrorMessage("An Error Occurred", error)
                }
            }.launchIn(viewModelScope)
    }

    private suspend fun newSearch() {
        // New search. Reset the state
        resetSearchState()

        searchRecipesUsecase.get().invoke(page = state.value.page, query = state.value.query)
            .onEach { dataState ->
                _state.update { it.copy(loading = dataState.loading) }

                dataState.data?.let { list ->
                    _state.update { it.copy(recipes = list) }
                }

                dataState.error?.let { error ->
                    showErrorDialog(error)
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

    private suspend fun nextPage(index: Int) {
        if ((index + 1) < (state.value.page * PAGE_SIZE) || state.value.loading) return
        if ((state.value.recipeListScrollPosition + 1) < (state.value.page * PAGE_SIZE)) return

        incrementPage()
        if (state.value.page <= 1) return

        fetchRecipes()
    }

    private suspend fun fetchRecipes() {
        searchRecipesUsecase.get()
            .invoke(page = state.value.page, query = state.value.query)
            .onEach { dataState ->
                _state.update { it.copy(loading = dataState.loading) }

                dataState.data?.let { list ->
                    appendRecipes(list)
                }

                dataState.error?.let { error ->
                    Timber.tag(TAG).e("nextPage: %s", error)
                    //                        dialogQueue.appendErrorMessage("An Error Occurred", error)
                }
            }.launchIn(viewModelScope)
    }

    /**
     * Append new recipes to the current list of recipes
     */
    private fun appendRecipes(recipes: List<Recipe>) {
        val current = ArrayList(state.value.recipes)
        current.addAll(recipes)
        _state.update { it.copy(recipes = current.toPersistentList()) }
    }

    private fun incrementPage() {
        setPage(state.value.page + 1)
    }

    private fun onChangeRecipeScrollPosition(position: Int) {
        setListScrollPosition(position = position)
    }

    /**
     * Called when a new search is executed.
     */
    private fun resetSearchState() {
        _state.update {
            it.copy(
                recipes = persistentListOf(),
                page = 1,
            )
        }
        onChangeRecipeScrollPosition(0)
        if (state.value.selectedCategory?.value != state.value.query) clearSelectedCategory()
    }

    private fun clearSelectedCategory() {
        setSelectedCategory(null)
        _state.update { it.copy(selectedCategory = null) }
    }

    private fun onQueryChanged(query: String) {
        setQuery(query)
    }

    private fun onSelectedCategoryChanged(category: String) {
        val newCategory = getFoodCategory(category)
        setSelectedCategory(newCategory)
        onQueryChanged(category)
    }

    private fun setListScrollPosition(position: Int) {
        _state.update { it.copy(recipeListScrollPosition = position) }
        savedStateHandle.set(STATE_KEY_LIST_POSITION, position)
    }

    private fun setPage(page: Int) {
        _state.update { it.copy(page = page) }
        savedStateHandle.set(STATE_KEY_PAGE, page)
    }

    private fun setSelectedCategory(category: FoodCategory?) {
        _state.update { it.copy(selectedCategory = category) }
        savedStateHandle.set(STATE_KEY_SELECTED_CATEGORY, category)
    }

    private fun setQuery(query: String) {
        _state.update { it.copy(query = query) }
        savedStateHandle[STATE_KEY_QUERY] = query
    }

    fun onCategoryScrollPositionChanged(position: Int, offset: Int) {
        _state.update { it.copy(categoryScrollPosition = position to offset) }
    }

    fun toggleDarkTheme() {
        settingsDataStore.toggleTheme()
    }

    companion object {
        const val PAGE_SIZE = 30

        const val STATE_KEY_PAGE = "recipe.state.page.key"
        const val STATE_KEY_QUERY = "recipe.state.query.key"
        const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
        const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"
    }
}
