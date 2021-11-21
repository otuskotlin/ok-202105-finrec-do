package com.finyou.fintrack.backend.validation

data class ValidationResult(
    val errors: List<IValidationError>
) {

    val isSuccess: Boolean
        get() = errors.isEmpty()

    companion object {
        val SUCCESS = ValidationResult(emptyList())
    }
}
