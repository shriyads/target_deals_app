package com.target.targetcasestudy.core.ui.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class UiStateHolder<T> {
    private val _uiState = MutableStateFlow<UIState<T>>(UIState.Loading)
    val uiState: StateFlow<UIState<T>> = _uiState.asStateFlow()

    fun setLoading() {
        _uiState.value = UIState.Loading
    }

    fun setSuccess(data: T) {
        _uiState.value = UIState.Success(data)
    }

    fun setError(throwable: Throwable, message: String? = null) {
        _uiState.value = UIState.Error(throwable, message)
    }

    fun reset() {
        _uiState.value = UIState.Idle
    }
}
