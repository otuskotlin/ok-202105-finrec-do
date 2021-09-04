package com.finyou.fintrack.backend.mapping.openapi

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.common.utils.doubleLet
import com.finyou.fintrack.openapi.models.*
import java.math.BigDecimal
import java.time.Instant

fun FtContext.setQuery(query: InitTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    operation = FinTransactionOperation.INIT
    appMode = query.debug?.mode.inner
    stubCase = query.debug?.stubCase.inner
}

fun FtContext.setQuery(query: CreateTransactionRequest) = apply {
    operation = FinTransactionOperation.CREATE
    onRequest = query.requestId ?: ""
    requestTransaction = query.createTransaction?.inner ?: FinTransactionModel.NONE
    appMode = query.debug?.mode.inner
    stubCase = query.debug?.stubCase.inner
}

fun FtContext.setQuery(query: ReadTransactionRequest) = apply {
    operation = FinTransactionOperation.READ
    onRequest = query.requestId ?: ""
    requestTransactionId = FinTransactionIdModel(query.readTransactionId ?: "")
    appMode = query.debug?.mode.inner
    stubCase = query.debug?.stubCase.inner
}

fun FtContext.setQuery(query: UpdateTransactionRequest) = apply {
    operation = FinTransactionOperation.UPDATE
    onRequest = query.requestId ?: ""
    requestTransaction = query.updateTransaction?.inner ?: FinTransactionModel.NONE
    appMode = query.debug?.mode.inner
    stubCase = query.debug?.stubCase.inner
}

fun FtContext.setQuery(query: DeleteTransactionRequest) = apply {
    operation = FinTransactionOperation.DELETE
    onRequest = query.requestId ?: ""
    requestTransactionId = FinTransactionIdModel(query.deleteTransactionId ?: "")
    appMode = query.debug?.mode.inner
    stubCase = query.debug?.stubCase.inner
}

fun FtContext.setQuery(query: SearchTransactionRequest) = apply {
    operation = FinTransactionOperation.SEARCH
    onRequest = query.requestId ?: ""
    requestPage = query.pagination?.inner ?: PaginatedRequestModel.NONE
    appMode = query.debug?.mode.inner
    stubCase = query.debug?.stubCase.inner
}

private val CreatableTransaction.inner: FinTransactionModel
    get() {
        return FinTransactionModel(
            userId = userId?.let { UserIdModel(it) } ?: UserIdModel.NONE,
            name = name ?: "",
            description = description ?: "",
            date = date?.let { Instant.parse(it) } ?: Instant.MIN,
            transactionType = transactionType?.inner ?: TypeModel.NONE,
            amountCurrency = doubleLet(amount, currency) { safeAmount, safeCurrency ->
                AmountCurrencyModel(BigDecimal.valueOf(safeAmount), safeCurrency)
            } ?: AmountCurrencyModel.NONE
        )
    }

private val UpdatableTransaction.inner: FinTransactionModel
    get() {
        return FinTransactionModel(
            id = FinTransactionIdModel(id ?: ""),
            userId = userId?.let { UserIdModel(it) } ?: UserIdModel.NONE,
            name = name ?: "",
            description = description ?: "",
            date = date?.let { Instant.parse(it) } ?: Instant.MIN,
            transactionType = transactionType?.inner ?: TypeModel.NONE,
            amountCurrency = doubleLet(amount, currency) { safeAmount, safeCurrency ->
                AmountCurrencyModel(BigDecimal.valueOf(safeAmount), safeCurrency)
            } ?: AmountCurrencyModel.NONE
        )
    }

private val PaginatedRequest.inner: PaginatedRequestModel
    get() {
        return PaginatedRequestModel(
            lastId = FinTransactionIdModel(lastId ?: ""),
            perPage = perPage ?: Int.MIN_VALUE
        )
    }

private val TransactionType.inner: TypeModel
    get() = when(this) {
        TransactionType.INCOME -> TypeModel.INCOME
        TransactionType.OUTCOME -> TypeModel.OUTCOME
    }

private val Debug.Mode?.inner: AppModeModel
    get() = when(this) {
        Debug.Mode.PROD -> AppModeModel.PROD
        Debug.Mode.TEST -> AppModeModel.TEST
        Debug.Mode.STUB -> AppModeModel.STUB
        null -> AppModeModel.PROD
    }

private val Debug.StubCase?.inner: StubCaseModel
    get() = when(this) {
        Debug.StubCase.SUCCESS -> StubCaseModel.SUCCESS
        Debug.StubCase.VALIDATION_ERROR -> StubCaseModel.VALIDATION_ERROR
        Debug.StubCase.PERMISSION_ERROR -> StubCaseModel.PERMISSION_ERROR
        Debug.StubCase.DB_ERROR -> StubCaseModel.DB_ERROR
        null -> StubCaseModel.NONE
    }
