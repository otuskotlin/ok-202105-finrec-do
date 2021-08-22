package com.finyou.fintrack.backend.models

@JvmInline
value class CurrencyModel(val currency: String) {
    companion object {
        val NONE = CurrencyModel("")
    }
}