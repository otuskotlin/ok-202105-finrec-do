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
    var status: CorStatus = CorStatus.NONE,
    var appMode: AppModeModel = AppModeModel.PROD,
    var stubCase: StubCaseModel = StubCaseModel.NONE,
    var operation: FinTransactionOperation = FinTransactionOperation.NONE,

    val userSession: IUserSession<*, *> = EmptySession,
) {
    fun addError(error: ErrorModel, failingStatus: Boolean = true) = apply {
        if (failingStatus) status = CorStatus.FAILING
        errors.add(error)
    }

    fun addError(error: Throwable, failingStatus: Boolean = true) = apply {
        addError(ErrorModel(message = error.message ?: "Error occurred"), failingStatus)
    }
}
