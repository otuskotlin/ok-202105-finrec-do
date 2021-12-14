package com.finyou.fintrack.backend.common.context

import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.repo.common.DbFinTransactionFilterRequest
import com.finyou.fintrack.backend.repo.common.IRepoFinTransaction

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

    var config: ContextConfig = ContextConfig(),
    var finTransactionRepo: IRepoFinTransaction = IRepoFinTransaction.NONE,
) {
    fun addError(error: ErrorModel, failingStatus: Boolean = true) = apply {
        if (failingStatus) status = CorStatus.FAILING
        errors.add(error)
    }

    fun addError(error: Throwable, failingStatus: Boolean = true) = apply {
        addError(ErrorModel(message = error.message ?: "Error occurred"), failingStatus)
    }

    val dbSearchFilter: DbFinTransactionFilterRequest
        get() = searchFilter
            .takeIf { it != FilterModel.NONE }
            ?.let { DbFinTransactionFilterRequest(filter = it) }
            ?: DbFinTransactionFilterRequest()
}
