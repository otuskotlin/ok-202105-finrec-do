package com.finyou.fintrack.backend.validation

interface IValidator<T> {
    fun validate(sample: T): ValidationResult
}