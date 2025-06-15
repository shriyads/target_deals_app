package com.target.targetcasestudy.core.utils.apiresult


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class ApiResultHandlerImpl : ApiResultHandler {

    override fun <T> handleApiCall(apiCallFunction: suspend () -> T): Flow<APIResult<T>> = flow {
        emit(APIResult.Loading) // Emit Loading state immediately
        try {
            val responseData = apiCallFunction.invoke() // Execute the actual API call

            if (responseData != null) {
                Timber.d("----------apiresultsuccess")
                emit(APIResult.Success(responseData))
            } else {
                Timber.d("-----------apiresulterror1")
                emit(
                    APIResult.Error(
                        exception = ApiException("API returned null data."),
                        apiMessage = "Received empty response from API."
                    )
                )
            }

        } catch (e: CancellationException) {
            Timber.e(e, "API call cancelled")
            throw e // Re-throw to propagate cancellation
        } catch (e: HttpException) {
            Timber.e(e, "HTTP Exception during API call: %s", e.code())
            val errorBody = e.response()?.errorBody()?.string()
            emit(
                APIResult.Error(
                    exception = e,
                    apiMessage = errorBody ?: e.message(),
                    apiErrorCode = e.code().toString()
                )
            )
        } catch (e: IOException) {
            Timber.e(e, "IO Exception during API call")
            emit(
                APIResult.Error(
                    exception = e,
                    apiMessage = "Network error. Please check your internet connection."
                )
            )
        } catch (e: Exception) {
            Timber.e(e, "Generic Exception during API call")
            emit(
                APIResult.Error(
                    exception = e, apiMessage = "An unexpected error occurred."
                )
            )
        }
    }
}

