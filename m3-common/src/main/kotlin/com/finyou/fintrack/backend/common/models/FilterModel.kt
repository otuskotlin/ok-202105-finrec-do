package com.finyou.fintrack.backend.common.models

import java.math.BigDecimal
import java.time.Instant

data class FilterModel(
    val dateStart: Instant = Instant.EPOCH,
    val dateEnd: Instant = Instant.MAX,
    val amountFrom: BigDecimal = BigDecimal.ZERO,
    val amountTo: BigDecimal = BigDecimal.valueOf(Double.MAX_VALUE),
    val transactionType: TypeModel = TypeModel.NONE,
) {

    val dateRange: LongRange
        get() = (if (dateStart == Instant.EPOCH) 0L
        else dateStart.toEpochMilli())..(if (dateEnd == Instant.MAX) Long.MAX_VALUE
        else dateEnd.toEpochMilli())

    companion object {
        val NONE = FilterModel()
    }
}
