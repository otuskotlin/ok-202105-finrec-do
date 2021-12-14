package com.finyou.fintrack.backend.common.models

import java.math.BigDecimal
import java.time.Instant

data class FilterModel(
    val dateStart: Instant = Instant.MIN,
    val dateEnd: Instant = Instant.MAX,
    val amountFrom: BigDecimal = BigDecimal.ZERO,
    val amountTo: BigDecimal = BigDecimal.valueOf(Double.MAX_VALUE),
    val transactionType: TypeModel = TypeModel.NONE,
) {

    val dateRange: LongRange
        get() {
            val dateStartLong = if (dateStart == Instant.MIN) Long.MIN_VALUE else dateStart.toEpochMilli()
            val dateEndLong = if (dateEnd == Instant.MAX) Long.MAX_VALUE else dateEnd.toEpochMilli()
            return dateStartLong..dateEndLong
        }

    companion object {
        val NONE = FilterModel()
    }
}
