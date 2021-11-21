package com.finyou.fintrack.backend.logic.chains.helpers

import com.finyou.fintrack.backend.common.context.CorStatus
import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.ErrorModel
import com.finyou.fintrack.backend.validation.IValidationFieldError
import com.finyou.fintrack.backend.validation.ValidationResult

fun FtContext.onValidationErrorHandle(result: ValidationResult) {
    if (result.isSuccess) return
    val validationErrors = result.errors.map {
        ErrorModel(
            message = it.message,
            field = if (it is IValidationFieldError) it.field else ""
        )
    }
    errors.addAll(validationErrors)
    status = CorStatus.FAILING
}