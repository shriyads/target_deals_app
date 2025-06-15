package com.target.targetcasestudy.core.ui.utils

sealed interface UIState<out T> {
    object Idle : UIState<Nothing>
    object Loading : UIState<Nothing>
    data class Success<T>(val data: T) : UIState<T>
    data class Error(val throwable: Throwable, val message: String? = null) : UIState<Nothing>
}
