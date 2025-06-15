package com.target.targetcasestudy.core.domain

import kotlinx.coroutines.flow.Flow

interface BaseUseCase<in Params, out Result> {
    operator fun invoke(params: Params): Flow<Result>
}