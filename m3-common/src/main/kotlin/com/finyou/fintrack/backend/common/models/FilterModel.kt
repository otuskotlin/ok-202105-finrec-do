package com.finyou.fintrack.backend.common.models

import java.math.BigDecimal
import java.time.Instant

data class FilterModel(
    val dateStart: Instant = Instant.MIN,
    val dateEnd: Instant = Instant.MIN,
    val amountFrom: BigDecimal = BigDecimal.ZERO,
    val amountTo: BigDecimal = BigDecimal.ZERO,
    val transactionType: TypeModel = TypeModel.NONE,
) {
    companion object {
        val NONE = FilterModel()
    }
}
