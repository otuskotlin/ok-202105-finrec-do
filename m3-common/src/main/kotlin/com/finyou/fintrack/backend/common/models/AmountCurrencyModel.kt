package com.finyou.fintrack.backend.common.models

import java.math.BigDecimal

data class AmountCurrencyModel(
    val amount: BigDecimal = BigDecimal.ZERO,
    val currency: String = "",
) {
    companion object {
        val NONE = AmountCurrencyModel()
    }
}