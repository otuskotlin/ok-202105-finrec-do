package com.finyou.fintrack.backend.mapping.mp

import com.finyou.fintrack.backend.common.context.FtContext
import com.finyou.fintrack.backend.common.models.*
import com.finyou.fintrack.backend.common.utils.Const
import com.finyou.fintrack.kmp.models.*
import java.util.*

fun FtContext.setQuery(query: InitTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
}

fun FtContext.setQuery(query: CreateTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestTransaction = query.createTransaction?.inner ?: TransactionModel.NONE
}

fun FtContext.setQuery(query: ReadTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestTransactionId = TransactionIdModel(query.readTransactionId ?: "")
}

fun FtContext.setQuery(query: UpdateTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestTransaction = query.updateTransaction?.inner ?: TransactionModel.NONE
}

fun FtContext.setQuery(query: DeleteTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestTransactionId = TransactionIdModel(query.deleteTransactionId ?: "")
}

fun FtContext.setQuery(query: SearchTransactionRequest) = apply {
    onRequest = query.requestId ?: ""
    requestPage = query.pagination?.inner ?: PaginatedRequestModel.NONE
}

private val CreatableTransaction.inner: TransactionModel
    get() {
        return TransactionModel(
            userId = userId?.let { UserIdModel(it) } ?: UserIdModel.NONE,
            name = name ?: "",
            description = description ?: "",
            date = convertCreateDate(date, time),
            transactionType = transactionType?.inner ?: TypeModel.NONE,
            amount = amount ?: Double.MIN_VALUE,
            currency = currency?.let { CurrencyModel(it) } ?: CurrencyModel.NONE
        )
    }

private val UpdatableTransaction.inner: TransactionModel
    get() {
        return TransactionModel(
            id = TransactionIdModel(id ?: ""),
            userId = userId?.let { UserIdModel(it) } ?: UserIdModel.NONE,
            name = name ?: "",
            description = description ?: "",
            date = convertCreateDate(date, time),
            transactionType = transactionType?.inner ?: TypeModel.NONE,
            amount = amount ?: Double.MIN_VALUE,
            currency = currency?.let { CurrencyModel(it) } ?: CurrencyModel.NONE
        )
    }

private val PaginatedRequest.inner: PaginatedRequestModel
    get() {
        return PaginatedRequestModel(
            lastId = TransactionIdModel(lastId ?: ""),
            perPage = perPage ?: Int.MIN_VALUE
        )
    }

private val TransactionType.inner: TypeModel
    get() = when(this) {
        TransactionType.INCOME -> TypeModel.INCOME
        TransactionType.OUTCOME -> TypeModel.OUTCOME
    }

internal fun convertCreateDate(date: String?, time: String?): Long {
    val transactionDate = Calendar.getInstance().apply {
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
    date?.let { safeDate ->
        val dateParts = safeDate.split(Const.DATE_SEPARATOR)
        if (dateParts.size == 3) {
            dateParts[0].toIntOrNull()?.let {
                transactionDate.set(Calendar.DAY_OF_MONTH, it)
            }
            dateParts[1].toIntOrNull()?.let {
                transactionDate.set(Calendar.MONTH, it - 1)
            }
            dateParts[2].toIntOrNull()?.let {
                transactionDate.set(Calendar.YEAR, it)
            }
        }
    }
    time?.let { safeTime ->
        val timeParts = safeTime.split(Const.TIME_SEPARATOR)
        if (timeParts.size == 2) {
            timeParts[0].toIntOrNull()?.let {
                transactionDate.set(Calendar.HOUR_OF_DAY, it)
            }
            timeParts[1].toIntOrNull()?.let {
                transactionDate.set(Calendar.MINUTE, it)
            }
        }
    }
    return transactionDate.timeInMillis
}