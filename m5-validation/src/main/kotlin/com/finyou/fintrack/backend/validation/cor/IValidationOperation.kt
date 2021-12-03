package com.finyou.fintrack.backend.validation.cor

interface IValidationOperation<C, T> {
    fun exec(context: C)
}