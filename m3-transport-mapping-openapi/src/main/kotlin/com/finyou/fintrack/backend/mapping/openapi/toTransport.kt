package com.finyou.fintrack.backend.mapping.openapi

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.openapi.models.*
import java.time.Instant

fun FtContext.toInitResponse() = InitTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) InitTransactionResponse.Result.ERROR
    else InitTransactionResponse.Result.SUCCESS
)

fun FtContext.toCreateResponse() = CreateTransactionResponse(
    requestId = onRequest,
    createdTransaction = responseTransaction.takeIf { it != FinTransactionModel.NONE }?.transport,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) CreateTransactionResponse.Result.ERROR
    else CreateTransactionResponse.Result.SUCCESS
)

fun FtContext.toReadResponse() = ReadTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) ReadTransactionResponse.Result.ERROR
    else ReadTransactionResponse.Result.SUCCESS,
    readTransaction = responseTransaction.takeIf { it != FinTransactionModel.NONE }?.transport
)

fun FtContext.toUpdateResponse() = UpdateTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) UpdateTransactionResponse.Result.ERROR
    else UpdateTransactionResponse.Result.SUCCESS,
    updatedTransaction = responseTransaction.takeIf { it != FinTransactionModel.NONE }?.transport
)

fun FtContext.toDeleteResponse() = DeleteTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) DeleteTransactionResponse.Result.ERROR
    else DeleteTransactionResponse.Result.SUCCESS,
    deletedTransaction = responseTransaction.takeIf { it != FinTransactionModel.NONE }?.transport
)

fun FtContext.toSearchResponse() = SearchTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) SearchTransactionResponse.Result.ERROR
    else SearchTransactionResponse.Result.SUCCESS,
    foundItems = responseTransactions.takeIf { it.isNotEmpty() }?.filter { it != FinTransactionModel.NONE }
        ?.map { it.transport },
    pagination = responsePage.takeIf { it != PaginatedResponseModel.NONE }?.transport
)

private val ErrorModel.transport: ApiError
    get() = ApiError(
        message = message.takeIf { it.isNotBlank() },
        field = field.takeIf { it.isNotBlank() }
    )

private val List<ErrorModel>.transport: List<ApiError>?
    get() = takeIf { it.isNotEmpty() }?.filter { it != ErrorModel.NONE }?.map { it.transport }

private val FinTransactionModel.transport: ResultTransaction
    get() = ResultTransaction(
        userId = userId.takeIf { it != UserIdModel.NONE }?.id,
        name = name.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() },
        date = date.takeIf { it.isAfter(Instant.EPOCH) }?.toString(),
        transactionType = transactionType.transport,
        amount = amountCurrency.takeIf { it != AmountCurrencyModel.NONE }?.amount?.toDouble(),
        currency = amountCurrency.takeIf { it != AmountCurrencyModel.NONE }?.currency,
        id = id.takeIf { it != FinTransactionIdModel.NONE }?.id,
        permissions = permissions.takeIf { it.isNotEmpty() }?.map { it.transport }?.toSet()
    )

private val TypeModel.transport: TransactionType?
    get() = when (this) {
        TypeModel.INCOME -> TransactionType.INCOME
        TypeModel.OUTCOME -> TransactionType.OUTCOME
        TypeModel.NONE -> null
    }

private val PermissionModel.transport: TransactionPermission
    get() = when (this) {
        PermissionModel.READ -> TransactionPermission.READ
        PermissionModel.UPDATE -> TransactionPermission.UPDATE
        PermissionModel.DELETE -> TransactionPermission.DELETE
    }

private val PaginatedResponseModel.transport: PaginatedResponse
    get() = PaginatedResponse(
        total = total.takeIf { it >= 0 },
        left = left.takeIf { it >= 0 },
        lastId = lastId.takeIf { it != FinTransactionIdModel.NONE }?.id
    )
