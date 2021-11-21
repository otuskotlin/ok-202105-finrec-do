package com.finyou.fintrack.backend.validation.cor

import com.finyou.fintrack.backend.cor.common.cor.CorDslMarker
import com.finyou.fintrack.backend.validation.IValidator
import com.finyou.fintrack.backend.validation.ValidationResult

@CorDslMarker
class ValidationOperationBuilder<C, T>(
    private var errorHandler: C.(ValidationResult) -> Unit = {}
) {
    private lateinit var onBlock: C.() -> T
    private lateinit var validator: IValidator<T>
    fun validator(validator: IValidator<T>) {
        this.validator = validator
    }
    fun on(block: C.() -> T) {
        onBlock = block
    }
    fun build(): IValidationOperation<C, T> {
        return DefaultValidationOperation(
            validator = validator,
            onBlock = onBlock,
            errorHandler = errorHandler
        )
    }
}