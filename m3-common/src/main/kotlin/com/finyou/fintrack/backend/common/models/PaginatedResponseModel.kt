package com.finyou.fintrack.backend.common.models

data class PaginatedResponseModel(
    val lastId: FinTransactionIdModel = FinTransactionIdModel.NONE,
    val total: Int = Int.MIN_VALUE,
    val left: Int = Int.MIN_VALUE,
) {
    companion object {
        val NONE = PaginatedResponseModel()
    }
}
