package com.finyou.fintrack.backend.common.models

import com.finyou.fintrack.backend.common.utils.dateToMillis
import java.math.BigDecimal
import java.time.Instant

data class FilterModel(
    var dateStart: Instant = Instant.MIN,
    var dateEnd: Instant = Instant.MAX,
    var amountFrom: BigDecimal = BigDecimal.ZERO,
    var amountTo: BigDecimal = BigDecimal.valueOf(Double.MAX_VALUE),
    var transactionType: TypeModel = TypeModel.NONE,
    var userId: UserIdModel = UserIdModel.NONE
) {

    val dateRange: LongRange
        get() = dateStart.dateToMillis..dateEnd.dateToMillis

    companion object {
        val NONE = FilterModel()
    }
}
