package com.finyou.fintrack.backend.common.models

data class PaginatedRequestModel(
    val lastId: TransactionIdModel = TransactionIdModel.NONE,
    val perPage: Int = Int.MIN_VALUE,
) {
    companion object {
        val NONE = PaginatedRequestModel()
    }
}
