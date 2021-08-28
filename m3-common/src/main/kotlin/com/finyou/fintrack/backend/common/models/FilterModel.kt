package com.finyou.fintrack.backend.common.models

data class FilterModel(
    val dateStart: Long = Long.MIN_VALUE,
    val dateEnd: Long = Long.MIN_VALUE,
    val amountFrom: Double = Double.MIN_VALUE,
    val amountTo: Double = Double.MIN_VALUE,
    val transactionType: TypeModel = TypeModel.NONE,
) {
    companion object {
        val NONE = FilterModel()
    }
}
