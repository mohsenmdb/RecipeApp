package com.me.recipe.ui.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.recipe.R
import com.me.recipe.domain.features.recipe.model.Recipe
import com.me.recipe.domain.features.recipelist.usecases.RestoreRecipesUsecase
import com.me.recipe.domain.features.recipelist.usecases.SearchRecipesUsecase
import com.me.recipe.shared.utils.FoodCategory
import com.me.recipe.shared.utils.RECIPE_PAGINATION_FIRST_PAGE
import com.me.recipe.shared.utils.RECIPE_PAGINATION_PAGE_SIZE
import com.me.recipe.shared.utils.TAG
import com.me.recipe.shared.utils.getFoodCategory
import com.me.recipe.ui.component.util.GenericDialogInfo
import com.me.recipe.ui.component.util.PositiveAction
import com.me.recipe.ui.navigation.RecipeListDestination
import com.me.recipe.ui.search.SearchContract.Event
import com.me.recipe.ui.search.SearchContract.Event.NewSearchEvent
import com.me.recipe.ui.search.SearchContract.Event.OnChangeRecipeScrollPosition
import com.me.recipe.ui.search.SearchContract.Event.OnQueryChanged
import com.me.recipe.ui.search.SearchContract.Event.OnRecipeClick
import com.me.recipe.ui.search.SearchContract.Event.OnRecipeLongClick
import com.me.recipe.ui.search.SearchContract.Event.OnSelectedCategoryChanged
import com.me.recipe.ui.search.SearchContract.Event.RestoreStateEvent
import com.me.recipe.ui.search.SearchContract.Event.SearchClearEvent
import com.me.recipe.util.errorformater.ErrorFormatter
import com.me.recipe.util.errorformater.exceptions.RecipeDataException
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRecipesUsecase: Lazy<SearchRecipesUsecase>,
    private val restoreRecipesUsecase: Lazy<RestoreRecipesUsecase>,
    private val savedStateHandle: SavedStateHandle,
    private val errorFormatter: Lazy<ErrorFormatter>,
) : ViewModel(), SearchContract {

    private val _state = MutableStateFlow(SearchContract.State(loading = true))
    override val state: StateFlow<SearchContract.State> = _state.asStateFlow()

    private val effectChannel = Channel<SearchContract.Effect>(Channel.UNLIMITED)
    override val effect: Flow<SearchContract.Effect> = effectChannel.receiveAsFlow()

    // sent by navigation args
    private val categoryTitleArg: String? =
        savedStateHandle[RecipeListDestination.CATEGORY_TITLE_ARG]

    override fun event(event: Event) {
        viewModelScope.launch {
            try {
                when (event) {
                    is NewSearchEvent -> fetchNewSearchRecipes()
                    is SearchClearEvent -> clearSearch()
                    is RestoreStateEvent -> restoreState()
                    is OnQueryChanged -> onQueryChanged(event.query)
                    is OnChangeRecipeScrollPosition -> onChangeRecipeScrollPosition(event.index)
                    is OnRecipeClick -> handleOnRecipeClicked(event.recipe)
                    is OnSelectedCategoryChanged ->
                        onSelectedCategoryChanged(event.category, event.position, event.offset)
                    is OnRecipeLongClick ->
                        effectChannel.trySend(SearchContract.Effect.ShowSnackbar(event.title))
                }
            } catch (e: Exception) {
                Timber.tag(TAG).e("Exception: %s", e)
                _state.update { it.copy(loading = false) }
                if (e.message != null) {
                    effectChannel.trySend(SearchContract.Effect.ShowSnackbar(e.message!!))
                }
            } finally {
                Timber.tag(TAG).d("launchJob: finally called.")
            }
        }
    }

    init {
        viewModelScope.launch {
            savedStateHandle.getStateFlow(STATE_KEY_PAGE, 1)
                .collectLatest { page ->
                    handleNewPage(page)
                }
        }
        viewModelScope.launch {
            savedStateHandle.getStateFlow(STATE_KEY_LIST_POSITION, 0)
                .collectLatest { page ->
                    handleRecipeListPositionChanged(page)
                }
        }
        viewModelScope.launch {
            savedStateHandle.getStateFlow(STATE_KEY_QUERY, "")
                .collectLatest { query ->
                    setNewSearchQuery(query)
                }
        }
        viewModelScope.launch {
            savedStateHandle.getStateFlow<FoodCategory?>(STATE_KEY_SELECTED_CATEGORY, null)
                .collectLatest { category ->
                    setSelectedCategory(category)
                }
        }

        when {
            state.value.recipeListScrollPosition != 0 ->
                event(RestoreStateEvent)

            !categoryTitleArg.isNullOrEmpty() ->
                event(OnSelectedCategoryChanged(categoryTitleArg))

            else ->
                event(NewSearchEvent)
        }
    }

    private fun handleOnRecipeClicked(recipe: Recipe) {
        try {
            if (recipe.id == Recipe.EMPTY.id) throw RecipeDataException()
            effectChannel.trySend(SearchContract.Effect.NavigateToRecipePage(recipe))
        } catch (e: Exception) {
            effectChannel.trySend(
                SearchContract.Effect.ShowSnackbar(errorFormatter.get().format(e)),
            )
        }
    }

    private fun handleRecipeListPositionChanged(position: Int) {
        setRecipeScrollPosition(position)
        if (checkReachEndOfTheList(position)) {
            increaseRecipePage()
        }
    }

    private suspend fun handleNewPage(page: Int) {
        setRecipeListPage(page)
        fetchNextRecipePage(page)
    }

    private suspend fun clearSearch() {
        onQueryChanged("")
        // delay to let query set empty, before isNewSearchSetBySelectingFromCategoryList() call
        delay(50)
        fetchNewSearchRecipes()
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

    private suspend fun fetchNewSearchRecipes() {
        resetSearchState()
        fetchRecipes()
    }

    private suspend fun fetchRecipes() {
        Timber.d("fetchRecipes page[%s] query[%s]", state.value.page, state.value.query)
        searchRecipesUsecase.get().invoke(page = state.value.page, query = state.value.query)
            .onEach { dataState ->
                _state.update { it.copy(loading = dataState.loading) }

                dataState.data?.let { list ->
                    appendRecipes(list)
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

    /**
     * Append new recipes to the current list of recipes
     */
    private fun appendRecipes(recipes: List<Recipe>) {
        val current = ArrayList(state.value.recipes)
        current.addAll(recipes)
        _state.update { it.copy(recipes = current.toPersistentList()) }
    }

    private fun onChangeRecipeScrollPosition(position: Int) {
        changeRecipeScrollPosition(position)
    }

    private fun changeRecipeScrollPosition(position: Int) {
        savedStateHandle[STATE_KEY_LIST_POSITION] = position
    }

    /**
     * Called when a new search is executed.
     */
    private fun resetSearchState() {
        clearRecipeList()
        changeRecipeListPage(RECIPE_PAGINATION_FIRST_PAGE)
        onChangeRecipeScrollPosition(INITIAL_RECIPE_LIST_POSITION)
        if (isNewSearchSetBySelectingFromCategoryList().not()) {
            changeSelectedCategory(null)
        }
    }

    private fun isNewSearchSetBySelectingFromCategoryList(): Boolean {
        return state.value.selectedCategory?.value == state.value.query
    }

    private fun clearRecipeList() {
        _state.update {
            it.copy(recipes = persistentListOf())
        }
    }

    private fun onQueryChanged(query: String) {
        savedStateHandle[STATE_KEY_QUERY] = query
    }

    private fun onSelectedCategoryChanged(category: String, position: Int, offset: Int) {
        changeSelectedCategory(getFoodCategory(category))
        onQueryChanged(category)
        onCategoryScrollPositionChanged(position, offset)
        viewModelScope.launch {
            fetchNewSearchRecipes()
        }
    }

    private fun setRecipeScrollPosition(position: Int) {
        _state.update { it.copy(recipeListScrollPosition = position) }
    }

    private fun checkReachEndOfTheList(position: Int): Boolean {
        if ((position + 1) < (state.value.page * RECIPE_PAGINATION_PAGE_SIZE) || state.value.loading) return false
        if ((state.value.recipeListScrollPosition + 1) < (state.value.page * RECIPE_PAGINATION_PAGE_SIZE)) return false
        return true
    }

    private fun increaseRecipePage() {
        changeRecipeListPage(state.value.page + 1)
    }

    private fun changeRecipeListPage(page: Int) {
        savedStateHandle[STATE_KEY_PAGE] = page
    }

    private fun setRecipeListPage(page: Int) {
        _state.update { it.copy(page = page) }
    }

    private suspend fun fetchNextRecipePage(page: Int) {
        if (page <= 1) return
        fetchRecipes()
    }

    private fun changeSelectedCategory(category: FoodCategory?) {
        savedStateHandle[STATE_KEY_SELECTED_CATEGORY] = category
    }

    private fun setSelectedCategory(category: FoodCategory?) {
        _state.update { it.copy(selectedCategory = category) }
    }

    private fun setNewSearchQuery(query: String) {
        _state.update { it.copy(query = query) }
    }

    private fun onCategoryScrollPositionChanged(position: Int, offset: Int) {
        _state.update { it.copy(categoryScrollPosition = position to offset) }
    }

    companion object {
        const val INITIAL_RECIPE_LIST_POSITION = 0

        const val STATE_KEY_PAGE = "recipe.state.page.key"
        const val STATE_KEY_QUERY = "recipe.state.query.key"
        const val STATE_KEY_LIST_POSITION = "recipe.state.query.list_position"
        const val STATE_KEY_SELECTED_CATEGORY = "recipe.state.query.selected_category"
    }
}
