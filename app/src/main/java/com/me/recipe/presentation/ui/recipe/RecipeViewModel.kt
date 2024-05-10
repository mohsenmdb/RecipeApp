package com.me.recipe.presentation.ui.recipe

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.me.recipe.domain.features.recipe.usecases.GetRecipeUsecase
import com.me.recipe.presentation.ui.navigation.RecipeDestination
import com.me.recipe.util.TAG
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
class RecipeViewModel @Inject constructor(
    private val getRecipeUsecase: Lazy<GetRecipeUsecase>,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), RecipeContract {

    private val _state = MutableStateFlow(RecipeContract.State(loading = true))
    override val state: StateFlow<RecipeContract.State> = _state.asStateFlow()

    private val effectChannel = Channel<RecipeContract.Effect>(Channel.UNLIMITED)
    override val effect: Flow<RecipeContract.Effect> = effectChannel.receiveAsFlow()

    override fun event(event: RecipeContract.Event) {}

    // sent by navigation args
    private val itemId: Int = checkNotNull(savedStateHandle[RecipeDestination.ITEM_ID_ARG])
    private val itemTitle: Int = checkNotNull(savedStateHandle[RecipeDestination.ITEM_TITLE_ARG])
    private val itemImage: Int = checkNotNull(savedStateHandle[RecipeDestination.ITEM_IMAGE_ARG])

    init {
        viewModelScope.launch {
            Timber.d("id[%s], itemTitle[%s], itemImage[%s]", itemId, itemTitle, itemImage)

            try {
                getRecipe(itemId)
            } catch (e: Exception) {
                _state.update { it.copy(loading = false) }
                if (e.message != null) {
                    effectChannel.trySend(RecipeContract.Effect.ShowSnackbar(e.message!!))
                }
            } finally {
                Timber.tag(TAG).d("launchJob: finally called.")
            }
        }
    }

    private suspend fun getRecipe(id: Int) {
        getRecipeUsecase.get().invoke(id, true).onEach { dataState ->
            _state.update { it.copy(loading = dataState.loading) }

            dataState.data?.let { recipe ->
                _state.update { it.copy(recipe = recipe) }
            }

            dataState.error?.let { error ->
//                dialogQueue.appendErrorMessage("An Error Occurred", error)
            }
        }.launchIn(viewModelScope)
        _state.update { it.copy(loading = false) }
    }
}
