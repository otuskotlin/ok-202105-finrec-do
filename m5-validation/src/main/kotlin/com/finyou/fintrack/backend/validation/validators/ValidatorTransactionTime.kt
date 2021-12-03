package com.finyou.fintrack.backend.validation.validators

import com.finyou.fintrack.backend.validation.IValidator
import com.finyou.fintrack.backend.validation.ValidationErrorField
import com.finyou.fintrack.backend.validation.ValidationResult
import java.time.Instant
import java.time.temporal.ChronoUnit

class ValidatorTransactionTime(
    private val field: String = "",
    private val message: String = "Time is not valid"
) : IValidator<Instant?> {

    override fun validate(sample: Instant?): ValidationResult {
        val now = Instant.now()
        return if (sample == null || sample.minus(1L, ChronoUnit.YEARS) > now)
            ValidationResult(
                errors = listOf(
                    ValidationErrorField(field = field, message = message)
                )
            )
        else ValidationResult.SUCCESS
    }
}