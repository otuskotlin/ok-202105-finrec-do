package com.finyou.fintrack.backend.mapping.openapi

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.common.utils.doubleLet
import com.finyou.fintrack.openapi.models.*
import java.math.BigDecimal
import java.time.Instant

fun FtContext.setQuery(query: InitTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
}

fun FtContext.setQuery(query: CreateTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestTransaction = query.createTransaction?.inner ?: FinTransactionModel.NONE
}

fun FtContext.setQuery(query: ReadTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestTransactionId = FinTransactionIdModel(query.readTransactionId ?: "")
}

fun FtContext.setQuery(query: UpdateTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestTransaction = query.updateTransaction?.inner ?: FinTransactionModel.NONE
}

fun FtContext.setQuery(query: DeleteTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestTransactionId = FinTransactionIdModel(query.deleteTransactionId ?: "")
}

fun FtContext.setQuery(query: SearchTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestPage = query.pagination?.inner ?: PaginatedRequestModel.NONE
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