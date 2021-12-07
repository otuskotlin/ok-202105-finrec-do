package com.finyou.fintrack.backend.repo.common

import com.finyou.fintrack.backend.common.models.ErrorModel

interface IDbResponse<T> {
    val isSuccess: Boolean
    val errors: List<ErrorModel>
    val result: T
}