package com.finyou.fintrack.backend.common.models

@JvmInline
value class CurrencyModel(val currency: String) {
    companion object {
        val NONE = CurrencyModel("")
    }
}