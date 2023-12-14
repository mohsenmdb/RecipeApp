package com.me.recipe.util.extention

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun Fragment.observeFlows(crossinline observationFunction: suspend (CoroutineScope) -> Unit) {
    viewLifecycleOwner.lifecycle.coroutineScope.launch {
        viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            observationFunction(this)
        }
    }
}

context(Fragment)
inline fun <T> Flow<T>.collectInFragment(
    crossinline onCollect: suspend (T) -> Unit
) = viewLifecycleOwner.lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        collectLatest {
            onCollect(it)
        }
    }
}

context (ViewModel)
inline fun <T> Flow<T>.collectStateInViewModel(
    defaultValue: T,
    crossinline onCollect: suspend (T) -> Unit
) {
    viewModelScope.launch {
        this@collectStateInViewModel
            .stateInDefault(viewModelScope, defaultValue)
            .collectLatest {
                onCollect(it)
            }
    }
}
