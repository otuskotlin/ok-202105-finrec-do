package com.finyou.fintrack.backend.common.context

import com.finyou.fintrack.backend.common.models.*

data class FtContext(
    var onRequest: String = "",
    var requestTransactionId: FinTransactionIdModel = FinTransactionIdModel.NONE,
    var requestTransaction: FinTransactionModel = FinTransactionModel.NONE,
    var responseTransaction: FinTransactionModel = FinTransactionModel.NONE,
    var requestPage: PaginatedRequestModel = PaginatedRequestModel.NONE,
    var responsePage: PaginatedResponseModel = PaginatedResponseModel.NONE,
    var searchFilter: FilterModel = FilterModel.NONE,
    var responseTransactions: MutableList<FinTransactionModel> = mutableListOf(),
    var errors: MutableList<ErrorModel> = mutableListOf(),
    var status: Status = Status.STARTED,
    var appMode: AppModeModel = AppModeModel.PROD,
    var stubCase: StubCaseModel = StubCaseModel.NONE
    )
