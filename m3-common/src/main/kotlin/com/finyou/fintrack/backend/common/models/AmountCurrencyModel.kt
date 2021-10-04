package com.finyou.fintrack.backend.common.models

import java.math.BigDecimal

data class AmountCurrencyModel(
    var amount: BigDecimal = BigDecimal.ZERO,
    var currency: String = "",
) {
    companion object {
        val NONE = AmountCurrencyModel()
    }
}