package com.finyou.fintrack.backend.common.models

import com.finyou.fintrack.backend.common.utils.dateToMillis
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
        get() = dateStart.dateToMillis..dateEnd.dateToMillis

    companion object {
        val NONE = FilterModel()
    }
}
