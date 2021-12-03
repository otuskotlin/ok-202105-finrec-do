package com.finyou.fintrack.backend.validation.validators

import com.finyou.fintrack.backend.validation.IValidator
import com.finyou.fintrack.backend.validation.ValidationErrorField
import com.finyou.fintrack.backend.validation.ValidationResult

class ValidatorStringNotEmpty(
    private val field: String = "",
    private val message: String = "String must not be empty"
) : IValidator<String?> {

    override fun validate(sample: String?) = if (sample.isNullOrBlank())
        ValidationResult(
            errors = listOf(
                ValidationErrorField(field = field, message = message)
            )
        )
    else ValidationResult.SUCCESS
}