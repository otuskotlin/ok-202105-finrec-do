package com.finyou.fintrack.backend.models

data class PaginatedResponseModel(
    val lastId: TransactionIdModel = TransactionIdModel.NONE,
    val total: Int = Int.MIN_VALUE,
    val left: Int = Int.MIN_VALUE,
) {
    companion object {
        val NONE = PaginatedResponseModel()
    }
}
