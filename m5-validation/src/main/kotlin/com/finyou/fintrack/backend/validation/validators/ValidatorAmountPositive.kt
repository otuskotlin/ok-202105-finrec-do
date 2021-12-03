package com.finyou.fintrack.backend.validation.validators

import com.finyou.fintrack.backend.validation.IValidator
import com.finyou.fintrack.backend.validation.ValidationErrorField
import com.finyou.fintrack.backend.validation.ValidationResult
import java.math.BigDecimal

class ValidatorAmountPositive(
    private val field: String = "",
    private val message: String = "Amount should be positive"
) : IValidator<BigDecimal?> {

    override fun validate(sample: BigDecimal?) = if (sample == null || sample <= 0.toBigDecimal())
        ValidationResult(
            errors = listOf(
                ValidationErrorField(field = field, message = message)
            )
        )
    else ValidationResult.SUCCESS
}