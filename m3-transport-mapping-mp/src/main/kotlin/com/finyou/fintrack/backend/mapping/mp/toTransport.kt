package com.finyou.fintrack.backend.mapping.mp

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.common.utils.Const
import com.finyou.fintrack.kmp.models.*
import java.util.*

fun FtContext.toInitResponse() = InitTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) InitTransactionResponse.Result.ERROR
    else InitTransactionResponse.Result.SUCCESS
)

fun FtContext.toCreateResponse() = CreateTransactionResponse(
    requestId = onRequest,
    createdTransaction = responseTransaction.transport,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) CreateTransactionResponse.Result.ERROR
    else CreateTransactionResponse.Result.SUCCESS
)

fun FtContext.toReadResponse() = ReadTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) ReadTransactionResponse.Result.ERROR
    else ReadTransactionResponse.Result.SUCCESS,
    readTransaction = responseTransaction.transport
)

fun FtContext.toUpdateResponse() = UpdateTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) UpdateTransactionResponse.Result.ERROR
    else UpdateTransactionResponse.Result.SUCCESS,
    updatedTransaction = responseTransaction.transport
)

fun FtContext.toDeleteResponse() = DeleteTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) DeleteTransactionResponse.Result.ERROR
    else DeleteTransactionResponse.Result.SUCCESS,
    deletedTransaction = responseTransaction.transport
)

fun FtContext.toSearchResponse() = SearchTransactionResponse(
    requestId = onRequest,
    errors = errors.transport,
    result = if (errors.isNotEmpty()) SearchTransactionResponse.Result.ERROR
    else SearchTransactionResponse.Result.SUCCESS,
    foundItems = responseTransactions.takeIf { it.isNotEmpty() }?.filter { it != TransactionModel.NONE }
        ?.map { it.transport },
    pagination = responsePage.takeIf { it != PaginatedResponseModel.NONE }?.transport
)

private val ErrorModel.transport: ApiError
    get() = ApiError(
        message = message.takeIf { it.isNotBlank() },
        field = field.takeIf { it.isNotBlank() }
    )

private val List<ErrorModel>.transport: List<ApiError>?
    get() = takeIf { it.isNotEmpty() }?.map { it.transport }

private val TransactionModel.transport: ResultTransaction
    get() = ResultTransaction(
        userId = userId.takeIf { it != UserIdModel.NONE }?.id,
        name = name.takeIf { it.isNotBlank() },
        description = description.takeIf { it.isNotBlank() },
        date = date.takeIf { it > 0 }?.toTransportDate(),
        time = date.takeIf { it > 0 }?.toTransportTime(),
        transactionType = transactionType.transport,
        amount = amount.takeIf { it >= 0 },
        currency = currency.currency.takeIf { it.isNotBlank() },
        id = id.takeIf { it != TransactionIdModel.NONE }?.id,
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

internal fun Long.toTransportDate(): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@toTransportDate
    }
    val day = String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH))
    val month = String.format("%02d", calendar.get(Calendar.MONTH) + 1)
    val year = String.format("%04d", calendar.get(Calendar.YEAR))
    return "$day${Const.DATE_SEPARATOR}$month${Const.DATE_SEPARATOR}$year"
}

internal fun Long.toTransportTime(): String {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = this@toTransportTime
    }
    val hour = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY))
    val minute = String.format("%02d", calendar.get(Calendar.MINUTE))
    return "$hour${Const.TIME_SEPARATOR}$minute"
}

private val PaginatedResponseModel.transport: PaginatedResponse
    get() = PaginatedResponse(
        total = total.takeIf { it >= 0 },
        left = left.takeIf { it >= 0 },
        lastId = lastId.takeIf { it != TransactionIdModel.NONE }?.id
    )
