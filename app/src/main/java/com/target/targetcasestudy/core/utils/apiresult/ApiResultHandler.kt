package com.target.targetcasestudy.core.utils.apiresult

import kotlinx.coroutines.flow.Flow

interface ApiResultHandler {
    fun <T> handleApiCall(apiCallFunction: suspend () -> T): Flow<APIResult<T>>
}