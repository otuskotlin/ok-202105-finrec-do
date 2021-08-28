package com.finyou.fintrack.backend.common.context

import com.finyou.fintrack.backend.common.models.*

data class FtContext(
    var onRequest: String = "",
    var requestTransactionId: TransactionIdModel = TransactionIdModel.NONE,
    var requestTransaction: TransactionModel = TransactionModel.NONE,
    var responseTransaction: TransactionModel = TransactionModel.NONE,
    var requestPage: PaginatedRequestModel = PaginatedRequestModel.NONE,
    var responsePage: PaginatedResponseModel = PaginatedResponseModel.NONE,
    var searchFilter: FilterModel = FilterModel.NONE,
    var responseTransactions: MutableList<TransactionModel> = mutableListOf(),
    var errors: MutableList<ErrorModel> = mutableListOf(),
    var status: Status = Status.STARTED
)
