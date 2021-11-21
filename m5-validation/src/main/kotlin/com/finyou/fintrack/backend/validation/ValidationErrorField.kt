package com.finyou.fintrack.backend.validation

data class ValidationErrorField(
    override val message: String,
    override val field: String,
) : IValidationFieldError