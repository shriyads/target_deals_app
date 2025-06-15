package com.target.targetcasestudy.core.utils.apiresult

sealed interface APIResult<out T> {

    data class Success<out T>(val data: T) : APIResult<T>

    data class Error(
        val exception: Exception,
        val apiErrorCode: String? = null,
        val apiMessage: String? = null
    ) :
        APIResult<Nothing>

    object Loading : APIResult<Nothing>
}

// Custom exception for API-level errors
class ApiException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)