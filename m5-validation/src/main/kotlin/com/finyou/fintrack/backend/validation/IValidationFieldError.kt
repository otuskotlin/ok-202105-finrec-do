package com.finyou.fintrack.backend.validation

interface IValidationFieldError : IValidationError {
    val field: String
}