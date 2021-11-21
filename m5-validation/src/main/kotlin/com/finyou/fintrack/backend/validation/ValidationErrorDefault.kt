package com.finyou.fintrack.backend.validation

data class ValidationErrorDefault(
    override val message: String,
) : IValidationError
